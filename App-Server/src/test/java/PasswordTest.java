import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;


public class PasswordTest {

    @Test
    void testPwdMatch(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 数据库密文
        String dbPwd = "$2a$10$Z8H1k8cJxO5R1kFd7X0Y6O9a2b3c4d5e6f7g8h9i0j";
        // 填入你前端登录输入的明文
        String rawPwd = "123456";
        boolean match = encoder.matches(rawPwd, dbPwd);
        System.out.println("是否匹配："+match);
    }

    // 额外：生成新加密密码
    @Test
    void createNewPwd(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        System.out.println("新加密密码：" + encode);
    }




}