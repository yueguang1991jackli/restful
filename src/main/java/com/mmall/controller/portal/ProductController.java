package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台商品模块
 * @author liliang
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;
    /**
     * 产品搜索及动态排序List
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/list.do")
    public ServerResponse<PageInfo<Product>> listProduct(Integer categoryId, String keyword,
                                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                                         @RequestParam(defaultValue = "")String orderBy){
        return productService.listProduct(categoryId,keyword,pageNum,pageSize,orderBy);
    }

    /**
     * 获取产品详细信息
     * @param productId
     * @return
     */
    @GetMapping("detail.do")
    public ServerResponse<Product> getDetail(Integer productId){
        return productService.getDetail(productId);
    }
}
