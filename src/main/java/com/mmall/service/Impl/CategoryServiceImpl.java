package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by guojianzhong on 2017/6/25.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

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

    @Override
    public ServerResponse selectCategoryById(Integer categoryId) {

        if (categoryId !=null) {
            List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);

            if (CollectionUtils.isEmpty(categories)) {
                logger.info("未找到当前的子分类");
            }
            return ServerResponse.createBySuccess(categories);

        }else {
            return ServerResponse.createByErrorMessage("当前分类id不能为空");
        }
    }

    @Override
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categories = Sets.newHashSet();

        findChildCategory(categories,categoryId);

        ArrayList<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            for (Category category: categories
                    ) {
                categoryIdList.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }




    @Override
    public Set<Category> findChildCategory(Set<Category> categories,Integer categoryId) {

        Category category = categoryMapper.selectByPrimaryKey(categoryId);

        if (category != null) {
            categories.add(category);
        }

        List<Category> categoriess = categoryMapper.selectCategoryChildrenByParentId(categoryId);

        for (Category categoryItem: categoriess
             ) {
            findChildCategory(categories,categoryItem.getId());
        }
        return categories;


    }



}
