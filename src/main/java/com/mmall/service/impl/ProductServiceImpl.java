package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 商品模块
 * @author liliang
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;

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

}
