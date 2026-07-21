package vo.admin;

import lombok.Data;

@Data
public class AdminVO {
    private Long id;
    private String username;
    private String name;
    private String token;
}
