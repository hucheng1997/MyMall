package com.hucheng.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.mall.order.entity.OrderSettingEntity;

import java.util.Map;

/**
 * 订单配置信息
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 14:58:12
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

