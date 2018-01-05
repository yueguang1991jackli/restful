package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.List;

/**
 * 后台品类管理模块
 * @author liliang
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取品类子节点(平级)
     * @param categoryId not null
     * @return
     */
    @Override
    public ServerResponse<List<Category>> getCategory(Integer categoryId) {
        //由parent id获取对应的产品集合
        List<Category> categoryList = categoryMapper.getCategoryByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            return ServerResponse.createByErrorMessage("未找到该品类");
        }   return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 添加品类
     * @param parentId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse addCategory(Integer parentId, String categoryName) {
        //添加数据,parentId,categoryName,主键自动生成
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setCreateTime(new Date(System.currentTimeMillis()));
        category.setUpdateTime(new Date(System.currentTimeMillis()));
        int result = categoryMapper.insertSelective(category);
        if (result == 1){
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    /**
     * 删除品类
     * @param id
     * @return
     */
    @Override
    public ServerResponse delCategory(Integer id) {
        int result = categoryMapper.deleteByPrimaryKey(id);
        if (result == 1){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse setCategory(Integer categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        category.setUpdateTime(new Date(System.currentTimeMillis()));
        int result = categoryMapper.updateByPrimaryKeySelective(category);
        if (result == 1){
            return ServerResponse.createBySuccessMessage("更新品类成功");
        }
        return ServerResponse.createByErrorMessage("更新品类失败");
    }

    /**
     * 递归获取品类的子节点
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Category>> getDeepCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.getCategoryByParentId(categoryId);
        return null;
    }
}
