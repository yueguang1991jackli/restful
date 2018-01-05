package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;

import java.util.List;

public interface ICategoryService {
    ServerResponse<List<Category>> getCategory(Integer categoryId);

    ServerResponse addCategory(Integer parentId, String categoryName);

    ServerResponse delCategory(Integer id);

    ServerResponse setCategory(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getDeepCategory(Integer categoryId);
}
