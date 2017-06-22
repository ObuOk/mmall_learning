package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by guojianzhong on 2017/6/22.
 */
@Service("iUserService")
    public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String name, String password) {

        //验证用户名是否存在
        int resulteCount = userMapper.checkUserName(name);

        if (resulteCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在。。。");
        }

        //// TODO: 2017/6/22  加密操作
        String md5Password = MD5Util.MD5EncodeUtf8(password);

       User user = userMapper.selectLogin(name,md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误。。。");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);

    }

    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse;

        validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int insert = userMapper.insert(user);
        if(insert == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

            return ServerResponse.createBySuccessMessage("注册成功");


    }

    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (type == Const.USERNAME) {
                int resultCount = userMapper.checkUserName(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (type == Const.EMAIL) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }

        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }




}
