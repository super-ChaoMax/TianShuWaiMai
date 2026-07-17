package entity.wx;

import lombok.Data;

@Data
public class AddressBook {
    private Long id;
    private Long userId;
    private String consignee;
    private String phone;
    private String detail;
    private String label;
    private Integer isDefault;
    private String createTime;
    private String updateTime;
    //version
    private Integer version;
    //deleted
    private Integer deleted;

    //新增

    private String sex;

    //    provinceCode: "44"
    private String provinceCode;
    //    provinceName: "广东省"
    private String provinceName;

    //    districtCode: "440883"
    private String districtCode;
    //    districtName: "吴川市"
    private String districtName;

    //    cityName: "湛江市"
    private String cityName;
    //    cityCode: "4408"
    private String cityCode;




//    sex: "0"



//    consignee: "zzt"
//    name: "zzt"
//    type: 2



}
