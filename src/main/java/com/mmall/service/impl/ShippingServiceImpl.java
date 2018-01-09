package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liliang
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse<Map<String,Integer>> createAddress(Integer userId, String receiverName, String receiverPhone,
                                                    String receiverMobile, String receiverProvince,
                                                    String receiverCity, String receiverAddress, String receiverZip) {
        //新建shipping对象,将传进来的变量赋值进去
        Shipping shipping = new Shipping();                   
        shipping.setUserId(userId);                           
        shipping.setReceiverName(receiverName);               
        shipping.setReceiverPhone(receiverPhone);             
        shipping.setReceiverMobile(receiverMobile);           
        shipping.setReceiverProvince(receiverProvince);       
        shipping.setReceiverCity(receiverCity);               
        shipping.setReceiverAddress(receiverAddress);         
        shipping.setReceiverZip(receiverZip);                 
        shipping.setCreateTime(new Date());                   
        shipping.setUpdateTime(new Date());                   
        //插入到数据库中,并注意时间的创建
        int result = shippingMapper.insertSelective(shipping);
        if (result == 1){
            //插入成功
            //创建成功后返回shippingId
            List<Integer> id = shippingMapper.selectByShipping(shipping);
            if (id.size()==1){
                Map<String, Integer> map = new HashMap<>(16);
                map.put("shippingId",id.get(0));
                return ServerResponse.createBySuccess("新建地址成功", map);
            }
            int delete = shippingMapper.deleteByPrimaryKey(id.get(1));
            return ServerResponse.createByErrorMessage("新建地址失败:地址已创建");
        }
        //否则返回新建地址失败
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse del(Integer shippingId) {
        int result = shippingMapper.deleteByPrimaryKey(shippingId);
        if (result == 1){
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败,请重试");
    }

    @Override
    public ServerResponse update(Shipping shipping) {
        shipping.setUpdateTime(new Date());
        int result = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (result == 1){
            return ServerResponse.createBySuccessMessage("更新成功");
        }return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer shippingId) {
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping!= null){
            return ServerResponse.createBySuccess(shipping);
        }
        return ServerResponse.createByErrorMessage("查询失败");
    }

    @Override
    public ServerResponse<PageInfo<Shipping>> list(Integer id, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList=shippingMapper.selectByUserId(id);
        if(shippingList==null){
            return ServerResponse.createBySuccessMessage("用户未创建收货地址");
        }else if (CollectionUtils.isEmpty(shippingList)){
            return ServerResponse.createBySuccessMessage("用户未创建收货地址");
        } else {
            PageInfo<Shipping> list = new PageInfo<>(shippingList);
            return ServerResponse.createBySuccess(list);
        }

    }
}
