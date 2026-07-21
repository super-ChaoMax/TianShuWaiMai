package dto.admin.employee;

import lombok.Data;

@Data
public class EmployeePageQueryDTO {
    // 页码
    private Integer page;
    // 每页条数
    private Integer pageSize;
    // 姓名模糊查询
    private String name;
}