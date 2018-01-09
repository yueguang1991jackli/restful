package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author liliang
 */
public interface IProductService {
    ServerResponse<PageInfo<Product>> listProduct(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

    ServerResponse<Product> getDetail(Integer productId);

    ServerResponse<List<Product>> listProducts(Integer pageNum, Integer pageSize);

    ServerResponse<PageInfo<ProductVo>> search(Integer productId, String productName, Integer pageNum, Integer pageSize);

    ServerResponse<Map<String,String>> upload(MultipartFile multipartFile,String path) throws IOException;
}
