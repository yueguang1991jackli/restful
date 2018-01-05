package com.mmall.controller.manage;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台商品管理模块
 * @author liliang
 */
@RestController
@RequestMapping("/manage/product")
public class ManageProductController {

    @Autowired
    IProductService productService;

    /**
     * 产品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list.do")
    public ServerResponse<List<Product>> listProducts(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return productService.listProducts(pageNum, pageSize);
    }
    @PostMapping("/search.do")
    public ServerResponse<PageInfo<ProductVo>> search(Integer productId, String productName,
                                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                                            @RequestParam(defaultValue = "10") Integer pageSize){
        return productService.search(productId, productName, pageNum, pageSize);
    }



}
