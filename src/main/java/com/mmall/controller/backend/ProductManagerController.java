package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductServer;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guojianzhong on 2017/6/27.
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductServer iProductServer;

    @Autowired
    private IFileService iFileService;

    @ResponseBody
    @RequestMapping("/productSave.do")
    public ServerResponse productSave(HttpSession session, Product product) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {

            return iProductServer.saveOrUpdateProduct(product);

        }else {

            return ServerResponse.createByErrorMessage("必须是管理员才有权限操作");
        }

    }
    @ResponseBody
    @RequestMapping("/setSaleStatus.do")
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        if (iUserService.checkAdminByRole(user).isSuccess()) {

            return iProductServer.setSaleStatus(productId,status);

        }else {

            return ServerResponse.createByErrorMessage("必须是管理员才有权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("/getDetail.do")
    public ServerResponse<ProductDetailVo> getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        if (iUserService.checkAdminByRole(user).isSuccess()) {

            return iProductServer.manageProductDetail(productId);

        }else {

            return ServerResponse.createByErrorMessage("必须是管理员才有权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("/list.do")
    public ServerResponse<PageInfo> getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {
            return iProductServer.getProductList(pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("必须是管理员才有权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("/searchProduct.do")
    public ServerResponse<PageInfo> searchProduct(HttpSession session,String productName, Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {
            return iProductServer.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("必须是管理员才有权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("/upload.do")
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file , HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {

            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);

        }else {
            return ServerResponse.createByErrorMessage("必须是管理员才有权限操作");
        }
    }

    @ResponseBody
    @RequestMapping("/richtext_img_upload.do")
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file , HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        if (iUserService.checkAdminByRole(user).isSuccess()) {

            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);

            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;

        }else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }



}
