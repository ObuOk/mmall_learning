package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by guojianzhong on 2017/6/22.
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserServiceImpl iUserService;

    /**
     * 登录接口
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {

        ServerResponse<User> login = iUserService.login(username, password);

        if (login.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,login.getData());
        }
        return iUserService.login(username,password);
    }

    /**
     * 登出接口
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("登出成功");
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("当前用户未登录，无法获取用户信息");
        }
            return ServerResponse.createBySuccess(user);
    }

    /**
     * 获得忘记密码问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 获得忘记密码问题的答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question, String answer) {
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 忘记密码-重置密码
     * @param username
     * @param passWordNew
     * @param token
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passWordNew, String token) {
        return iUserService.forgetResetPassword(username,passWordNew,token);
    }

    /**
     * 重置密码
     * @param passwordOld
     * @param passWordNew
     * @param session
     * @return
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String passwordOld,String passWordNew, HttpSession session) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passWordNew,user);
    }

    /**
     * 更新个人信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "updateUserInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session, User user) {

        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);

        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());

        ServerResponse<User> response = iUserService.updateUserInfo(user);
        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 根据session获得个人信息
     * @param session
     * @return
     */
    public ServerResponse<User> getInfomation(HttpSession session) {

        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);

        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }

        return iUserService.getInfomation(currentUser.getId());


    }





}
