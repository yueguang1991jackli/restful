package com.mmall.controller.manage;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    /**
     * 商品搜索
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/search.do")
    public ServerResponse<PageInfo<ProductVo>> search(Integer productId, String productName,
                                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                                            @RequestParam(defaultValue = "10") Integer pageSize){
        return productService.search(productId, productName, pageNum, pageSize);
    }

    /**
     * 文件上传
     * @param request
     * @param upload_file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload.do")
    public ServerResponse<Map<String,String>> upload(HttpServletRequest request,MultipartFile upload_file) throws IOException {
        String path = request.getSession().getServletContext().getRealPath("pic");
        return productService.upload(upload_file,path);
    }


}
