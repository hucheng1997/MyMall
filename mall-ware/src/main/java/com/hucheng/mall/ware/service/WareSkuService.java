package com.hucheng.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hucheng.common.to.OrderTo;
import com.hucheng.common.to.mq.StockLockedTo;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.mall.ware.entity.WareSkuEntity;
import com.hucheng.mall.ware.vo.SkuHasStockVo;
import com.hucheng.mall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 15:02:10
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    boolean orderLockStock(WareSkuLockVo vo);

    /**
     * 解锁库存
     * @param to
     */
    void unlockStock(StockLockedTo to);
    /**
     * 解锁订单
     * @param orderTo
     */
    void unlockStock(OrderTo orderTo);
}

