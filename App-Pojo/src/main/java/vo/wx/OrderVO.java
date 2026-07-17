package vo.wx;


import lombok.Data;

@Data
public class OrderVO {
    /**
     * 订单ID（强烈建议转为 String）
     * 原因：前端 JavaScript 处理 Java 的 Long 类型时，由于精度问题会导致末尾数字变成 0。转为 String 是最安全的做法。
     */
    private Long id;

    /**
     * 外部订单流水号
     * 用于前端展示给用户看（如：复制订单号、显示在支付页面）
     */
    private String orderNumber;



    /**
     * 订单实付金额（格式化后的字符串）
     * 例如："￥28.50"。直接带上货币符号，方便前端直接渲染。
     */
    private String orderAmount;


    /**
     * 订单创建时间（格式化后的字符串）
     * 例如："2023-10-01 14:30:00"。后端直接转好格式，避免前端做复杂的时区处理。
     */
    private String orderTime;


}