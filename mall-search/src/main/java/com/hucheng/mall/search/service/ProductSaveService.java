package com.hucheng.mall.search.service;

import com.hucheng.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author MrHu
 */
public interface ProductSaveService {

    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
