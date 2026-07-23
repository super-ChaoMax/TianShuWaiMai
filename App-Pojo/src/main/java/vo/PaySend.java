package vo;

import lombok.Data;

@Data
public class PaySend {
    //给二维码支付页面的参数
    private Integer type;
    private String orderNo;
    private String msg;
}
