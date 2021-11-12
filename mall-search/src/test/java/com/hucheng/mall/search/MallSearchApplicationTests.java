package com.hucheng.mall.search;

import com.alibaba.fastjson.JSON;
import com.hucheng.mall.search.config.MallElasticSearchConfig;
import lombok.*;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallSearchApplicationTests {

    @Resource
    private RestHighLevelClient client;

    @Test
    public void testSearchApi() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        //封装检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery())
                .aggregation(AggregationBuilders.terms("ageAgg").field("age").size(100)
                        .subAggregation(AggregationBuilders.terms("genderAgg").field("gender.keyword").size(2)
                                .subAggregation(AggregationBuilders.avg("balanceAvg").field("balance")))
                        .subAggregation(AggregationBuilders.avg("balanceAvg2").field("balance")));
        System.out.println(searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, MallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(search);
    }

    @Test
    public void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("users2", "1");
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    public void testDocument() throws IOException {
        User user = new User("Jack","M",18);
        IndexRequest indexRequest = new IndexRequest("users2").id("1").source(JSON.toJSONString(user), XContentType.JSON);
        client.index(indexRequest,RequestOptions.DEFAULT);
    }

    @Test
    public  void testIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("users2")
                .source(JSON.toJSONString(new User()), XContentType.JSON);
        client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class User {
        private String userName;
        private String gender;
        private Integer age;
    }

    @Test
    public void contextLoads() {
        System.out.println(client);
    }
}
