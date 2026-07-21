package dto.admin.employee;

import lombok.Data;
// 员工新增 / 编辑 DTO
@Data
public class EmployeeDTO {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Integer sex;
    private String idNumber;

    //再加一个乐观锁
    // 加上这个！！
    private Long version;
}