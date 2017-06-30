package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by guojianzhong on 2017/6/25.
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);


    ServerResponse updateCategoryName(String categoryName, Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

    Set<Category> findChildCategory(Set<Category> categories, Integer categoryId);

    ServerResponse selectCategoryById(Integer categoryId);
}
