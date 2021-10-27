package com.hucheng.mall.member;

import com.hucheng.common.utils.R;
import com.hucheng.mall.member.feign.CouponFeignService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MallMemberApplicationTests {

    @Autowired
    CouponFeignService couponFeignService;

    @Test
    public void contextLoads() {
        Object coupon = couponFeignService.memberCoupon().get("coupon");
        System.out.println(coupon);
    }

}
