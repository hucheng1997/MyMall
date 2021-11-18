package com.hucheng.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.hucheng.common.to.es.SkuEsModel;
import com.hucheng.mall.search.config.MallElasticSearchConfig;
import com.hucheng.mall.search.constant.EsConstant;
import com.hucheng.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrHu
 */
@Slf4j
@Service("productSaveService")
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    private RestHighLevelClient esRestClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        //1、在es中建立索引，建立号映射关系（doc/json/product-mapping.json）
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest("users2")
//                .source(JSON.toJSONString(new HashMap<>()), XContentType.JSON);
//        esRestClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        //2、在ES中保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX)
                    .id(skuEsModel.getSkuId().toString());
            String jsonString = JSON.toJSONString(skuEsModel);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = esRestClient.bulk(bulkRequest, MallElasticSearchConfig.COMMON_OPTIONS);
        //如果存在错误
        boolean hasFailures = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(BulkItemResponse::getId).collect(Collectors.toList());
//        log.info("商品上架完成：{}", collect);
        return hasFailures;
    }
}
