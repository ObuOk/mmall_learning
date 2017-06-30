package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * Created by guojianzhong on 2017/6/29.
 */
public interface ICartService {

    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer id, Integer productId, Integer count);

    ServerResponse<CartVo> delete(Integer id, String productId);

    ServerResponse<CartVo> selectOrUnselect(Integer userId, Integer productId,Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
