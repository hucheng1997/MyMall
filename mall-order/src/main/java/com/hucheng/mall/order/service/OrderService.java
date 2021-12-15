package com.hucheng.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.mall.order.entity.OrderEntity;
import com.hucheng.mall.order.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 14:58:12
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 订单确认页返回需要用的数据
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException ;

    /**
     * 创建订单
     * @param vo
     * @return
     */
    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);

    /**
     * 关闭订单
     * @param orderEntity
     */
    void closeOrder(OrderEntity orderEntity);
    /**
     * 按照订单号获取订单信息
     * @param orderSn
     * @return
     */
    OrderEntity getOrderByOrderSn(String orderSn);

    /**
     * 获取当前订单的支付信息
     * @param orderSn
     * @return
     */
    PayVo getOrderPay(String orderSn);

    /**
     * 查询当前用户所有订单数据
     * @param params
     * @return
     */
    PageUtils queryPageWithItem(Map<String, Object> params);

    String handlePayResult(PayAsyncVo asyncVo);
    /**
     * 微信异步通知处理
     * @param notifyData
     */
    String asyncNotify(String notifyData);
}

