package com.chaao.appserver.service.Impl.wx;


import com.chaao.appserver.mapper.wx.AddressBookMapper;
import com.chaao.appserver.mapper.wx.OrderDetailMapper;
import com.chaao.appserver.mapper.wx.OrdersMapper;
import com.chaao.appserver.mapper.wx.ShoppingCartMapper;
import com.chaao.appserver.service.AddressBookService;
import com.chaao.appserver.service.OrderService;
import com.yourcompany.common.constant.OrdersConstant;
import com.yourcompany.common.util.XueHuaiID;
import dto.wx.OrderSubmitDTO;
import entity.wx.AddressBook;
import entity.wx.OrderDetail;
import entity.wx.Orders;
import entity.wx.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.wx.OrderVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper orderMapper;

    //地址mapper
    @Autowired
    private AddressBookMapper addressBookMapper;

    //订单详情mapper
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    //购物车
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    /**
     * 提交订单
     */
    @Override
    @Transactional      //事务锁
    public OrderVO submitOrder(OrderSubmitDTO orderSubmitDTO) {
        // 1. 构建订单实体对象
        Orders orders = new Orders();



        // 使用 BeanUtils 拷贝同名属性 (如 addressBookId, remark 等)
        BeanUtils.copyProperties(orderSubmitDTO, orders);
        
        // 2. 【关键】手动设置餐具相关字段
        // 因为 DTO 中的字段名与 Entity 可能不完全一致，或者需要特殊处理
        orders.setTablewareStatus(orderSubmitDTO.getTablewareStatus());
        orders.setTablewareNumber(orderSubmitDTO.getTablewareNumber());





    // 3. 其他必要字段的填充 (如订单号、时间、状态等)

//        获取当前订单唯一 id
        Long orderId = XueHuaiID.generateUserId();

        // 生成订单号雪花id（把orderId字符串变成long）
        orders.setId( orderId );

        //设置订单流水号（再加上用户id前6位）
        //用你原来的 "ORD" + 日期 + 流水号，专门用来给用户看、打印在小票上。
        orders.setNumber(XueHuaiID.generateOrderNo()+orderSubmitDTO.getUserId().toString().substring(0,6) );

        //设置状态(待付款)
        orders.setStatus(OrdersConstant.PENDING_PAYMENT);

        //设置金额
        orders.setAmount(orderSubmitDTO.getAmount());
        //直接设置实付金额
        orders.setPayAmount(orderSubmitDTO.getAmount());

        //乐观锁初始化
        orders.setVersion(1);

        //立即送达/预计送达
        if(orderSubmitDTO.getDeliveryStatus()==1){
            //立即送出
            orders.setDeliveryTime(LocalDateTime.now()); // 立即送出 LocalDateTime
        }else{
            // 获取前端传来的时间字符串，例如 "11:00"
            String estimatedTime = orderSubmitDTO.getEstimatedDeliveryTime();

            LocalDateTime deliveryDateTime = null;
            if (estimatedTime != null && !estimatedTime.isEmpty()) {
                // 1. 获取今天的日期
                LocalDate today = LocalDate.now();
                // 2. 解析前端传来的时间 "11:00"
                LocalTime time = LocalTime.parse(estimatedTime);
                // 3. 将日期和时间组合成完整的 LocalDateTime
                deliveryDateTime = LocalDateTime.of(today, time);
            }

            // 4. 将完整的日期时间设置到订单对象中
            orders.setDeliveryTime(deliveryDateTime);
        }





        //解析并存储好地址的快照
        AddressBook  addressBook=addressBookMapper.getById( orderSubmitDTO.getAddressBookId());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail());


        orders.setOrderTime(LocalDateTime.now());

        // 4. 插入数据库
        orderMapper.insert(orders);


// 5. 订单详情表插入

        //查询出购物车数据
        //查出当前购物车的数据
        List<ShoppingCart> cartList = shoppingCartMapper.getShoppingCartList(orderSubmitDTO.getUserId());
        log.info("查出的购物车数据：{}", cartList);
        //创建一个集合
        List<OrderDetail> orderDetailList = new ArrayList<>();

        //遍历购物车数据
        for (ShoppingCart cart : cartList) {
            // 构建订单详情实体对象
            OrderDetail orderDetail = new OrderDetail();

            // 1. 拷贝同名属性（如 name, image, dishId, number, amount 等）
            BeanUtils.copyProperties(cart, orderDetail);

            // 2. 【核心修复】手动将 id 置为 null，防止购物车主键污染订单明细主键
            orderDetail.setId(XueHuaiID.generateUserId());

            // 3. 清除创建时间，让数据库使用 DEFAULT CURRENT_TIMESTAMP 自动生成
            orderDetail.setCreateTime(null);

            // 4. 关联新生成的订单主表ID
            orderDetail.setOrderId(orders.getId());

            // 添加进集合
            orderDetailList.add(orderDetail);
        }

        orderSubmitDTO.setOrderDetails(orderDetailList);

        log.info("订单详情对象：{}", orderDetailList);
        log.info("订单对象：{}", orders);

        // ... (此处省略金额计算和地址查询逻辑)

        log.info("前端传过来的：{}", orderSubmitDTO);
        log.info("准备存储的订单对象：{}", orders);



        orderDetailMapper.insertBatch(orderDetailList);

        // 5. 返回结果 VO (此处简化处理，实际应查询详情返回)
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        return orderVO;
    }
}