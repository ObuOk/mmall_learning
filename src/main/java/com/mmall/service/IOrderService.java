package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;

import java.util.HashMap;

/**
 * Created by guojianzhong on 2017/7/1.
 */
public interface IOrderService {


    ServerResponse pay(Long orderNo, Integer id, String path);

    ServerResponse aliCallback(HashMap<String, String> params);

    ServerResponse queryOrderPayStatis(Integer id, Long orderNo);

    ServerResponse createOrder(Integer id, Integer shippingId);

    ServerResponse cancel(Integer id, Long orderNo);

    ServerResponse getOrderCartProduct(Integer id);

    ServerResponse getOrderDetail(Integer id, Long orderNo);

    ServerResponse getOrderList(Integer id, int pageNum, int pageSize);

    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    ServerResponse manageDetail(Long orderNo);

    ServerResponse manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse sendGoods(Long orderNo);
}
