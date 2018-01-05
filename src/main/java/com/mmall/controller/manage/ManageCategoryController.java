package com.mmall.controller.manage;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 分类管理模块
 * @author liliang
 */
@RestController
@RequestMapping("/manage/category")
public class ManageCategoryController {
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 获取子品类信息
     * @param categoryId
     * @param session
     * @return
     */
    @GetMapping("/get_category.do")
    public ServerResponse<List<Category>> getCategory(@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId,
                                                      HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(10,"用户未登录,请登录");
        }
        return iCategoryService.getCategory(categoryId);
    }

    /**
     * 添加品类
     * @param parentId
     * @param categoryName
     * @return
     */
    @PostMapping("/add_category.do")
    public ServerResponse addCategory(@RequestParam(value = "parentId",defaultValue = "0") Integer parentId,String categoryName){
        return iCategoryService.addCategory(parentId,categoryName);
    }

    /**
     * 删除品类名称
     * @param id
     * @return
     */
    @GetMapping("/del_category.do")
    public ServerResponse delCategory(Integer id){
        return iCategoryService.delCategory(id);
    }

    /**
     * 修改品类名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    @PostMapping("/set_category_name.do")
    public ServerResponse setCategory(Integer categoryId,String categoryName){
        return iCategoryService.setCategory(categoryId, categoryName);
    }

    @GetMapping("/get_deep_category.do")
    public ServerResponse<List<Category>> getDeepCategory(Integer categoryId){
        return iCategoryService.getDeepCategory(categoryId);
    }

}
