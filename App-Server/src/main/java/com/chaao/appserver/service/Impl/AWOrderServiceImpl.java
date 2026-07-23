package com.chaao.appserver.service.Impl;

import com.chaao.appserver.mapper.wx.OrderDetailMapper;
import com.chaao.appserver.mapper.wx.OrdersMapper;
import com.chaao.appserver.service.AWOrderService;
import entity.wx.OrderDetail;
import entity.wx.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.admin.OrderInfoVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AWOrderServiceImpl implements AWOrderService {

    @Autowired
    OrdersMapper ordersmapper;

    @Autowired
    OrderDetailMapper orderdetailmapper;


    @Override
    public OrderInfoVO getOrderInfoVO(Long id) {

        Orders orders=ordersmapper.getById(id);
        log.info("查到的订单详情"+orders);

        List<OrderDetail> ListOrderDetail=orderdetailmapper.getByOrderId(id);
        log.info("查到的菜单"+ListOrderDetail);
        // 使用 BeanUtils 拷贝同名属性 (如 addressBookId, remark 等)
        OrderInfoVO infoVO=new OrderInfoVO();
//        BeanUtils.copyProperties(infoVO, orders);

        //状态
        infoVO.setStatus(orders.getStatus());
        infoVO.setOrderNo(orders.getNumber());
        infoVO.setUserName(orders.getConsignee());
        infoVO.setUserPhone(orders.getPhone());
        infoVO.setUserAddress(orders.getAddress());
        infoVO.setCreateTime(orders.getCreateTime().toString());
        //待修改
        infoVO.setPayTypeText("微信支付");

        infoVO.setRemark(orders.getRemark());


        //总费
        infoVO.setGoodsTotal(orders.getAmount().doubleValue());
        //配送费
        infoVO.setDeliveryFee(3.0);
        //优化
        infoVO.setCouponPrice(orders.getDiscountAmount().doubleValue());
        //实付
        infoVO.setPayTotal(orders.getPayAmount().doubleValue());

        infoVO.setStatus( orders.getStatus());



        // 2. 创建商品集合
        List<OrderInfoVO.OrderGoods> goodsList = new ArrayList<>();


        for(OrderDetail orderDetail :ListOrderDetail){
            OrderInfoVO.OrderGoods goods = new OrderInfoVO.OrderGoods();
            goods.setGoodsName(orderDetail.getName());
            goods.setGoodsNum(orderDetail.getNumber());
            goods.setGoodsPrice(orderDetail.getAmount().doubleValue());
            goodsList.add(goods);
        }
        infoVO.setGoodsList(goodsList);

        return infoVO;
    }
}
