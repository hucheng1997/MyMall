package com.hucheng.mall.ware.feign;

import com.hucheng.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author MrHu
 */
@FeignClient("mall-order")
public interface OrderFeignService {

    @GetMapping(value = "/order/order/status/{orderSn}")
    R getOrderStatus(@PathVariable("orderSn") String orderSn);
}
