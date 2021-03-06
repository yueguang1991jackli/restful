package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 商品模块
 * @author liliang
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Value("ftp.url")
    private String ftpUrl;

    private String defaultOrderBy = "asc";

    @Autowired
    ProductMapper productMapper;

    public static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    /**
     * 产品搜索,分两种情况,一种是品类搜索,一种是模糊查询的搜索
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse<PageInfo<Product>> listProduct(Integer categoryId, String keyword, Integer pageNum,
                                                         Integer pageSize, String orderBy) {
        final int beginIndex = 6;
        String orderByInfo = orderBy.substring(beginIndex,orderBy.length());
            //1 根据category id进行查询,此时keyword对应category名称
        if (categoryId != null){
            //根据产品分类id,进行查询,子类产品
                //查询的产品进行分页
                PageHelper.startPage(pageNum,pageSize);
                List<Product> list = productMapper.selectByCategoryId(categoryId,orderByInfo);
                if (CollectionUtils.isEmpty(list)){
                    return ServerResponse.createByErrorMessage("无相关产品");
                }
                PageInfo<Product> pageInfo = new PageInfo<>(list);
                //返回分页数据
                return ServerResponse.createBySuccess(pageInfo);
        }else{
            //2 根据keyword 进行模糊查询,categoryId 为空
            //查询出的产品进行分
            PageHelper.startPage(pageNum,pageSize);
            //对产品名称进行模糊查询
            List<Product> list = productMapper.selectByName(keyword,orderByInfo);
            if (CollectionUtils.isEmpty(list)){
                return ServerResponse.createByErrorMessage("无相关产品");
            }
            PageInfo<Product> pageInfo = new PageInfo<>(list);
            //返回分页数据
            return ServerResponse.createBySuccess(pageInfo);
        }
    }

    /**
     * 获取产品信息
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<Product> getDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product != null){
            if (product.getStatus()==1){
                return ServerResponse.createBySuccess(product);
            }
            return ServerResponse.createByErrorMessage("该商品已下架或删除");
        }return ServerResponse.createByErrorMessage("该商品已下架或删除");
    }

    @Override
    public ServerResponse<List<Product>> listProducts(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectAllProduct();
        if (CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorMessage("查询失败");
        }
        return ServerResponse.createBySuccess(productList);
    }

    @Override
    public ServerResponse<PageInfo<ProductVo>> search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        if(productId == null&& productName != null){
            PageHelper.startPage(pageNum, pageSize);
            List<ProductVo> productList = productMapper.selectByNameVo(productName);
            if (CollectionUtils.isEmpty(productList)){
                return ServerResponse.createByErrorMessage("查询失败");
            }else {
                PageInfo<ProductVo> pageInfo = new PageInfo<>(productList);
                return ServerResponse.createBySuccess(pageInfo);
            }
        }else if (productId != null&& productName == null){
            PageHelper.startPage(pageNum, pageSize);
            ProductVo product = productMapper.selectByKey(productId);
            if (product == null){
                return ServerResponse.createByErrorMessage("品类未找到,请重试");
            }else {
                List<ProductVo> productList = new ArrayList<>();
                productList.add(product);
                PageInfo<ProductVo> pageInfo = new PageInfo<>(productList);
                return ServerResponse.createBySuccess(pageInfo);
            }
        }
        return ServerResponse.createByErrorMessage("参数错误");
    }

    @Override
    public ServerResponse<Map<String, String>> upload(MultipartFile upload_file,String path) throws IOException {
        if (upload_file != null){
            //保存到本地
            //获取名称,并生成随机名称
            String originalFilename = upload_file.getOriginalFilename();
            String fileExtName = originalFilename.substring(originalFilename.lastIndexOf("."),originalFilename.length());
            String uuid= UUID.randomUUID().toString();
            String fileName = uuid+fileExtName;
            File file = new File(path+"/"+fileName);
            boolean newFile = file.createNewFile();
            if (newFile){
                upload_file.transferTo(file);
                Map<String,String> resultMap = new HashMap<>(16);
                resultMap.put("uri",fileName);
                resultMap.put("url",file.getPath());
                    //返回文件保存路径,已共后续访问
                return ServerResponse.createBySuccess(resultMap);
            }
            logger.error("创建文件失败,可能没有权限");
        }
        return ServerResponse.createByError();
    }

}
