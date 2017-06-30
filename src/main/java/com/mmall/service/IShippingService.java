package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * Created by guojianzhong on 2017/6/30.
 */
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse delete(Integer id, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse select(Integer id, Integer shippingId);

    ServerResponse list(Integer id, Integer pageNum, Integer pageSize);

}
