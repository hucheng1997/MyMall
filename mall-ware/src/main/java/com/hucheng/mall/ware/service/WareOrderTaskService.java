package com.hucheng.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.mall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 15:02:10
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}

