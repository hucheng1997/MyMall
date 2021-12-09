package com.hucheng.mall.cart.service;

import com.hucheng.mall.cart.vo.CartItemVo;
import com.hucheng.mall.cart.vo.CartVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author MrHu
 */
public interface CartService {
    /**
     * 获取购物车里面的信息
     */
    CartVo getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车的数据
     * @param cartKey
     */
    void clearCartInfo(String cartKey);

    /**
     * 获取购物车某个购物项
     * @param skuId
     * @return
     */
    CartItemVo getCartItem(Long skuId);
    /**
     * 将商品添加至购物车
     * @param skuId
     * @param num
     * @return
     */
    CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 勾选购物项
     * @param skuId
     * @param checked
     */
    void checkItem(Long skuId, Integer checked);

    void deleteIdCartInfo(Long skuId);

    /**
     * 改变商品数量
     * @param skuId
     * @param num
     */
    void changeItemCount(Long skuId, Integer num);

    List<CartItemVo> getUserCartItems();
}
