package com.hucheng.mall.coupon.dao;

import com.hucheng.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 14:46:04
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
