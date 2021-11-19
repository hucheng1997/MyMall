package com.hucheng.mall.search.service;

import com.hucheng.mall.search.vo.SearchParam;

import com.hucheng.mall.search.vo.SearchResult;

/**
 * @author MrHu
 */
public interface MallSearchService {
    SearchResult search(SearchParam param);
}
