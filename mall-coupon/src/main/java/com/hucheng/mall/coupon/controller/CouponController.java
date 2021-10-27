package com.hucheng.mall.coupon.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hucheng.mall.coupon.entity.CouponEntity;
import com.hucheng.mall.coupon.service.CouponService;
import com.hucheng.common.utils.PageUtils;
import com.hucheng.common.utils.R;



/**
 * 优惠券信息
 *
 * @author hucheng
 * @email hucheng@gmail.com
 * @date 2021-10-27 14:46:04
 */
@RefreshScope
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @Value(("${coupon.user.name}"))
    private String userName;

    @Value(("${coupon.user.age}"))
    private String userAge;

    @RequestMapping("/user")
    public R user(){
        return R.ok().put("userName",userName).put("userAge",userAge);
    }

    @RequestMapping("/member/list")
    public R memberCoupon(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("5折");
        return R.ok().put("coupon",Collections.singletonList(couponEntity));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
