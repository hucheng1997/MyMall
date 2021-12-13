package com.hucheng.mall.order.vo;

import com.hucheng.mall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author MrHu
 */
@Data
public class SubmitOrderResponseVo {
    private OrderEntity order;

    /** 错误状态码 */
    private Integer code;
}
