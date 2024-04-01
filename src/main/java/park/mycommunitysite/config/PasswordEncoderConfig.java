package park.mycommunitysite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    // 비밀번호 인코더는 여러곳에서 사용하기에
    // 순환참조 오류 발생이 쉬워 따로 비밀번호 설정 클래스를 작성하였다.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
