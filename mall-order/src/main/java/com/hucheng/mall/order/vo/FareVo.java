package com.hucheng.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author MrHu
 */
@Data
public class FareVo {
    private MemberAddressVo address;

    private BigDecimal fare;
}
