package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.sun.corba.se.spi.activation.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by guojianzhong on 2017/6/23.
 */

@Controller
@RequestMapping("/manager/user")
public class UserManagerController {

        @Autowired
        private IUserService iUserService;

        @RequestMapping(value = "/login.do", method = RequestMethod.POST)
        @ResponseBody
        public ServerResponse<User> login(String username, String password, HttpSession session) {
            ServerResponse<User> login = iUserService.login(username, password);
            if (login.isSuccess()) {
                User data = login.getData();
                if (data.getRole().equals(Const.Role.ROLE_CUSTOMER)) {
                    return ServerResponse.createByErrorMessage("不是管理员，无法登录");
                }
                return login;
            }
            return login;
        }

}
