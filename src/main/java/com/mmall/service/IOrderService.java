package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.HashMap;

/**
 * Created by guojianzhong on 2017/7/1.
 */
public interface IOrderService {


    ServerResponse pay(Long orderNo, Integer id, String path);

    ServerResponse aliCallback(HashMap<String, String> params);

    ServerResponse queryOrderPayStatis(Integer id, Long orderNo);
}
