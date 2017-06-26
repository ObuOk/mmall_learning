package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by guojianzhong on 2017/6/25.
 */

@Controller
    @RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @ResponseBody
    @RequestMapping("/addCategory.do")
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") Integer parentId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {

            return iCategoryService.addCategory(categoryName,parentId);

        }else {
            return ServerResponse.createByErrorMessage("添加失败，用户必须是管理员才可以添加");
        }
    }

    @ResponseBody
    @RequestMapping("/updateCategoryName.do")
    public ServerResponse setCategoryName(HttpSession session, String categoryName, Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {

            return iCategoryService.updateCategoryName(categoryName,categoryId);

        }else {
            return ServerResponse.createByErrorMessage("更新失败，用户必须是管理员才可以更新品类");
        }
    }

    @ResponseBody
    @RequestMapping("/getChildParallelCategory.do")
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {

            return iCategoryService.selectCategoryById(categoryId);

        }else {
            return ServerResponse.createByErrorMessage("查询失败，用户必须是管理员才可以查询品类");
        }
    }
    @ResponseBody
    @RequestMapping("/getChildrenDeepCategory.do")
    public ServerResponse getChildrenDeepCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {

            return iCategoryService.selectCategoryAndChildrenById(categoryId);

        }else {
            return ServerResponse.createByErrorMessage("查询失败，用户必须是管理员才可以查询品类");
        }
    }



}
