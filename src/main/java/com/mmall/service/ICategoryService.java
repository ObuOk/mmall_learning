package com.mmall.service;

import com.mmall.common.ServerResponse;
import org.springframework.stereotype.Service;

/**
 * Created by guojianzhong on 2017/6/25.
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);


    ServerResponse updateCategoryName(String categoryName, Integer categoryId);

}
