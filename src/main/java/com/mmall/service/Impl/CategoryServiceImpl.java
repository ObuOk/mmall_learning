package com.mmall.service.Impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by guojianzhong on 2017/6/25.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    public ServerResponse addCategory(String categoryName, Integer parentId) {

        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();

        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int insert = categoryMapper.insert(category);
        if (insert > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
            return ServerResponse.createByErrorMessage("添加品类失败");

    }

    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {

        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int updateCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
            return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

}
