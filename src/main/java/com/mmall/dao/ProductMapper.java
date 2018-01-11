package com.mmall.dao;

import com.mmall.pojo.Product;
import com.mmall.vo.ProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectByCategoryId(@Param("categoryId") Integer categoryId,@Param("orderBy") String orderBy);

    List<Product> selectByName(@Param("name") String keyword, @Param("orderBy") String orderByInfo);

    List<Product> selectAllProduct();

    List<ProductVo> selectByNameVo(String name);

    ProductVo selectByKey(Integer productId);

    Product selectByProductId(Integer key);
}