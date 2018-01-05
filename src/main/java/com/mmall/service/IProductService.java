package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * @author liliang
 */
public interface IProductService {
    ServerResponse<PageInfo<Product>> listProduct(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

    ServerResponse<Product> getDetail(Integer productId);
}
