package com.hucheng.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hucheng.mall.product.service.CategoryBrandRelationService;
import com.hucheng.mall.product.vo.Catalogs2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.common.utils.Query;

import com.hucheng.mall.product.dao.CategoryDao;
import com.hucheng.mall.product.entity.CategoryEntity;
import com.hucheng.mall.product.service.CategoryService;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> tree() {
        List<CategoryEntity> categoryEntityList = this.list(null);
        return categoryEntityList.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0L)
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, categoryEntityList)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMenusByIds(List<Long> singletonList) {
        baseMapper.deleteBatchIds(singletonList);
    }

    @Override
    public Long[] getCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = this.findParentPath(catelogId, paths);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);

        return parentPath.toArray(new Long[parentPath.size()]);
    }

    @Override
    public List<CategoryEntity> getLevel1Categories() {
        System.out.println("get Level 1 Categories........");
        long l = System.currentTimeMillis();
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(
                new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        System.out.println("消耗时间：" + (System.currentTimeMillis() - l));
        return categoryEntities;
    }

    @Override
    public Map<String, List<Catalogs2Vo>> getCatalogJson() {
        // 1.从缓存中读取分类信息
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            // 2. 缓存中没有，查询数据库
            Map<String, List<Catalogs2Vo>> catalogJsonFromDB = getCatalogJsonFromDB();
            // 3. 查询到的数据存放到缓存中，将对象转成 JSON 存储
            redisTemplate.opsForValue().set("catalogJSON", JSON.toJSONString(catalogJsonFromDB));
            return catalogJsonFromDB;
        }
        return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catalogs2Vo>>>() {
        });
    }

    /**
     * 加缓存前,只读取数据库的操作
     *
     * @return
     */
    private Map<String, List<Catalogs2Vo>> getCatalogJsonFromDB() {
        System.out.println("查询了数据库");

        // 性能优化：将数据库的多次查询变为一次
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);

        //1、查出所有分类
        //1、1）查出所有一级分类
        List<CategoryEntity> level1Categories = getParentCid(selectList, 0L);

        Map<String, List<Catalogs2Vo>> parentCid = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1、每一个的一级分类,查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParentCid(selectList, v.getCatId());
            //2、封装上面的结果
            List<Catalogs2Vo> catalogs2Vos = null;
            if (categoryEntities != null) {
                catalogs2Vos = categoryEntities.stream().map(l2 -> {
                    Catalogs2Vo catalogs2Vo = new Catalogs2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1、找当前二级分类的三级分类封装成vo
                    List<CategoryEntity> level3Catelog = getParentCid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catalogs2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                            //2、封装成指定格式
                            Catalogs2Vo.Category3Vo category3Vo = new Catalogs2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                            return category3Vo;
                        }).collect(Collectors.toList());
                        catalogs2Vo.setCatalog3List(category3Vos);
                    }
                    return catalogs2Vo;
                }).collect(Collectors.toList());
            }
            return catalogs2Vos;
        }));
        return parentCid;
    }

    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList, long parentCid) {
        return selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity entity = this.getById(catelogId);
        if (entity.getParentCid() != 0L){
            this.findParentPath(entity.getParentCid(), paths);
        }
        //父节点放在前面
        Collections.reverse(paths);
        return paths;
    }

    /**
     * 获取子节点
     */
    private List<CategoryEntity> getChildren(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntityList) {
        return categoryEntityList.stream().filter(t -> t.getParentCid().equals(categoryEntity.getCatId()))
                .peek(child -> child.setChildren(getChildren(child, categoryEntityList)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

}