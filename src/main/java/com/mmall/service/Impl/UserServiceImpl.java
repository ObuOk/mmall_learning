package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    @Override
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
        user.setRole(Const.Role.ROLE_ADMIN);

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int insert = userMapper.insert(user);
        if(insert == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
            return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
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

    @Override
    public ServerResponse<String> selectQuestion(String username) {

        ServerResponse<String> checkReponse = checkValid(username, Const.USERNAME);
        if (checkReponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestion(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("问题不存在或为空");

    }

    @Override
    public ServerResponse<String> checkAnswer(String username,String question,String answer) {
        int answerCount = userMapper.checkAnswer(username, question, answer);

        if (answerCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
            return ServerResponse.createByErrorMessage("问题答案错误");


    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passWordNew, String token) {
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token不能为空");
        }
        ServerResponse<String> stringServerResponse = checkValid(username, Const.USERNAME);
        if (stringServerResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token2 = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);

        if (StringUtils.isBlank(token2)) {
            return ServerResponse.createByErrorMessage("token过期或者不存在");
        }

        if (StringUtils.equals(token,token2)) {
            String passwordMD5 = MD5Util.MD5EncodeUtf8(passWordNew);
            int updateCount = userMapper.updatePasswordByUserName(username, passwordMD5);
            if (updateCount > 0) {
                return ServerResponse.createBySuccess("修改密码成功");
            }
        }else {
                return ServerResponse.createByErrorMessage("token错误");
        }
                return ServerResponse.createByErrorMessage("修改密码失败");

    }
    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passWordNew, User user) {

        int selectCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (selectCount == 0) {
            return ServerResponse.createByErrorMessage("用户不存在,或者密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passWordNew));

        int updateCount = userMapper.updateByPrimaryKeySelective(user);
            if (updateCount > 0) {
                return ServerResponse.createBySuccess("修改密码成功");
            }
                return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {

        int selectCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());

        if (selectCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已被占用");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("用户信息修改成功",updateUser);
        }
            return ServerResponse.createByErrorMessage("用户信息修改失败");


    }

    @Override
    public ServerResponse<User> getInfomation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<User> checkAdminByRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuceess();
        }

        return ServerResponse.createByError();

    }
}
