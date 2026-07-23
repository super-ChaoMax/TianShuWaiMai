package com.chaao.appserver.mapper.wx;



import entity.wx.Orders;
import log.wx.PayLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PayLogMapper {

    //提供给网站的用的根据用户id查出最后一个订单
    /**
     * 根据 userId 查询当前用户最新一条订单ID
     * @param userId 用户id
     * @return 最新订单id
     */
//    Long  getLastOrderIdByUserId(@Param("userId") Long userId);
    // 不再只查id，查id和amount
    Orders getLastOrderInfoByUserId(@Param("userId") Long userId);


    /**
     * 根据 提供的 id 查询出订单
     * @param id 订单id
     * @return 最新订单id
     */
    Orders getById(@Param("id") Long id);


    // 新增支付退款日志
    int insertPayLog(@Param("log") PayLog payLog);
}