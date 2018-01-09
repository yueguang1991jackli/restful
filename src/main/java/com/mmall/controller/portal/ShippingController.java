package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author liliang
 */
@RestController
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    IShippingService iShippingService;

    /**
     * 新建地址
     * @param userId
     * @param receiverName
     * @param receiverPhone
     * @param receiverMobile
     * @param receiverProvince
     * @param receiverCity
     * @param receiverAddress
     * @param receiverZip
     * @return
     */
    @PostMapping("/add.do")
    public ServerResponse<Map<String,Integer>> add(Integer userId,
                                                   String receiverName,
                                                   String  receiverPhone,
                                                   String  receiverMobile,
                                                   String receiverProvince,
                                                   String receiverCity,
                                                   String receiverAddress,
                                                   String  receiverZip) {
        return iShippingService.createAddress(userId, receiverName, receiverPhone, receiverMobile,
                receiverProvince, receiverCity, receiverAddress, receiverZip);
    }

    /**
     * 删除地址
     * @param shippingId
     * @return
     */
    @GetMapping("/del.do")
    public ServerResponse del(Integer shippingId){
        return iShippingService.del(shippingId);
    }

    /**
     * 更新地址
     * @param shipping
     * @return
     */
    @PostMapping("/update.do")
    public ServerResponse update(Shipping shipping){
        return iShippingService.update(shipping);
    }

    /**
     * 选中查看具体的地址
     * @param shippingId
     * @return
     */
    @GetMapping("/select.do")
    public ServerResponse<Shipping> select(Integer shippingId){
        return iShippingService.select(shippingId);
    }
    @GetMapping("/list.do")
    public ServerResponse<PageInfo<Shipping>> list(HttpServletRequest request,@RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "10")Integer pageSize){
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
        Integer id = user.getId();
        return iShippingService.list(id,pageNum, pageSize);
    }
}
