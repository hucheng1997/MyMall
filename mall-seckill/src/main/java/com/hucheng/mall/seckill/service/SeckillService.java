package com.hucheng.mall.seckill.service;

import com.hucheng.mall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @author MrHu
 */
public interface SeckillService {
    /**
     * 上架三天需要秒杀的商品
     */
    void uploadSeckillSkuLatest3Days();

    /**
     * 当前时间可以参与秒杀的商品信息
     */
    List<SeckillSkuRedisTo> getCurrentSeckillSkus();
}
