package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by guojianzhong on 2017/6/22.
 */
public interface IUserService {

    ServerResponse<User> login(String name, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username, String passWordNew, String token);

    ServerResponse<String> resetPassword(String passwordOld, String passWordNew, User user);

    ServerResponse<User> updateUserInfo(User user);

    ServerResponse<User> getInfomation(Integer id);

    ServerResponse<User> checkAdminByRole(User id);
}
