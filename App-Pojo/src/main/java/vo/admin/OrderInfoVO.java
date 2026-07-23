package vo.admin;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * 订单详情返回VO
 */
@Getter
@Setter
public class OrderInfoVO {

    /**
     * 订单状态 1待付款 2待接单 3配送中 4已完成 5已取消 6退款中
     */
    private Integer status;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 收货人姓名
     */
    private String userName;

    /**
     * 收货人手机号
     */
    private String userPhone;

    /**
     * 收货地址
     */
    private String userAddress;

    /**
     * 下单时间
     */
    private String createTime;

    /**
     * 支付方式文字
     */
    private String payTypeText;

    /**
     * 用户备注
     */
    private String remark;

    /**
     * 商品合计金额
     */
    private Double goodsTotal;

    /**
     * 配送费
     */
    private Double deliveryFee;

    /**
     * 优惠券减免金额
     */
    private Double couponPrice;

    /**
     * 实付金额
     */
    private Double payTotal;



    /**
     * 订单商品列表
     */
    private List<OrderGoods> goodsList;




    /**
     * 订单商品内部VO
     * 必须：public static 静态内部类，外面才能直接 new / 赋值
     */
    @Getter
    @Setter
    public static class OrderGoods {

        /**
         * 商品名称
         */
        private String goodsName;

        /**
         * 购买数量
         */
        private Integer goodsNum;

        /**
         * 商品单价
         */
        private Double goodsPrice;

    }

}