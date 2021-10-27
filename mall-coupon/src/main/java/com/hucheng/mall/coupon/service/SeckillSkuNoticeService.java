package com.hucheng.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.mall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 14:46:04
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

