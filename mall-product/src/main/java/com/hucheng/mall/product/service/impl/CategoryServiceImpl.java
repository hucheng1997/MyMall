package com.hucheng.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hucheng.mall.product.service.CategoryBrandRelationService;
import com.hucheng.mall.product.vo.Catalogs2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.common.utils.Query;

import com.hucheng.mall.product.dao.CategoryDao;
import com.hucheng.mall.product.entity.CategoryEntity;
import com.hucheng.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
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
    @Caching(evict = {
            @CacheEvict(value = "category",key = "'getLevel1Categorys'"),
            @CacheEvict(value = "category",key = "'getCatelogJson'")
    })
    @Transactional
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
    @Cacheable(value = {"category"},key = "#root.method.name")
    public List<CategoryEntity> getLevel1Categories() {
        System.out.println("get Level 1 Categories........");
        long l = System.currentTimeMillis();
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(
                new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        System.out.println("???????????????" + (System.currentTimeMillis() - l));
        return categoryEntities;
    }

    @Cacheable(value = "category",key = "#root.methodName")
    @Override
    public Map<String, List<Catalogs2Vo>> getCatalogJson() {
        // 1.??????????????????????????????
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            // 2. ?????????????????????????????????
            Map<String, List<Catalogs2Vo>> catalogJsonFromDB = getCatalogJsonFromDB();
            // 3. ?????????????????????????????????????????????????????? JSON ??????
            redisTemplate.opsForValue().set("catalogJSON", JSON.toJSONString(catalogJsonFromDB));
            return catalogJsonFromDB;
        }
        return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catalogs2Vo>>>() {
        });
       
    }

    /**
     * ?????????????????????????????????::????????????
     *
     * @return
     */
    public Map<String, List<Catalogs2Vo>> getCatalogJsonFromDbWithRedisLock() {
        //1????????????????????????redis??????      ?????????????????????????????????????????????????????????????????????????????????
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            System.out.println("????????????????????????...");
            Map<String, List<Catalogs2Vo>> dataFromDb;
            try {
                //????????????...????????????
                dataFromDb = getCatalogJsonFromDB();
            } finally {
                // lua ????????????
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                // ?????????
                redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("lock"), uuid);
            }
            return dataFromDb;
        } else {
            System.out.println("????????????????????????...????????????...");
            //????????????...????????????
            //??????????????????
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDbWithRedisLock();     //???????????????
        }
    }

    /**
     * ????????????,???????????????????????????
     *
     * @return
     */
    private Map<String, List<Catalogs2Vo>> getCatalogJsonFromDB() {
        System.out.println("??????????????????");

        // ??????????????????????????????????????????????????????
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);

        //1?????????????????????
        //1???1???????????????????????????
        List<CategoryEntity> level1Categories = getParentCid(selectList, 0L);

        Map<String, List<Catalogs2Vo>> parentCid = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1???????????????????????????,???????????????????????????????????????
            List<CategoryEntity> categoryEntities = getParentCid(selectList, v.getCatId());
            //2????????????????????????
            List<Catalogs2Vo> catalogs2Vos = null;
            if (categoryEntities != null) {
                catalogs2Vos = categoryEntities.stream().map(l2 -> {
                    Catalogs2Vo catalogs2Vo = new Catalogs2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1????????????????????????????????????????????????vo
                    List<CategoryEntity> level3Catelog = getParentCid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catalogs2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                            //2????????????????????????
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
        //?????????????????????
        Collections.reverse(paths);
        return paths;
    }

    /**
     * ???????????????
     */
    private List<CategoryEntity> getChildren(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntityList) {
        return categoryEntityList.stream().filter(t -> t.getParentCid().equals(categoryEntity.getCatId()))
                .peek(child -> child.setChildren(getChildren(child, categoryEntityList)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

}