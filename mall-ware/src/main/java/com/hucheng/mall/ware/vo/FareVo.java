package com.hucheng.mall.ware.vo;

import com.hucheng.mall.ware.vo.MemberAddressVo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yaoxinjia
 */
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}


