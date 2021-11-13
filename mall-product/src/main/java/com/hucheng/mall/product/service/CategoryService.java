package com.hucheng.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.mall.product.entity.CategoryEntity;
import com.hucheng.mall.product.vo.Catalogs2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 11:17:22
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> tree();

    void removeMenusByIds(List<Long> singletonList);

    Long[] getCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    Long[] findCatelogPath(Long catelogId);

    List<CategoryEntity> getLevel1Categories();

    Map<String, List<Catalogs2Vo>> getCatalogJson();
}

