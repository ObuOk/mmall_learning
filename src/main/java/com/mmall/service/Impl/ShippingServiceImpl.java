package com.mmall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojianzhong on 2017/6/30.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int resultCount = shippingMapper.insert(shipping);
        if (resultCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shipping",shipping.getId());
            return ServerResponse.createBySuccess("添加收获地址成功",result);
        }
        return ServerResponse.createByErrorMessage("添加收获地址失败");
    }

    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {

        int resultCount = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }
            return ServerResponse.createByErrorMessage("删除地址失败");

    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {

        int resultCount = shippingMapper.updateByUserId(shipping);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功");
        }
            return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {

        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping != null) {
            return ServerResponse.createBySuccess(shipping);
        }
            return ServerResponse.createByErrorMessage("无法查找到该地址");
    }

    @Override
    public ServerResponse list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);

        PageInfo pageInfo = new PageInfo(shippings);

        return ServerResponse.createBySuccess(pageInfo);

    }

}
