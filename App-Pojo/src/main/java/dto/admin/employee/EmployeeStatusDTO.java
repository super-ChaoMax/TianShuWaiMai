package dto.admin.employee;

import lombok.Data;

//状态修改 DTO
@Data
public class EmployeeStatusDTO {
    private Long id;
    private Integer status;
}