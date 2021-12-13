package com.hucheng.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author MrHu
 */
@Data
public class WareSkuLockVo {

    private String orderSn;

    /** 需要锁住的所有库存信息 **/
    private List<OrderItemVo> locks;
}
