package park.mycommunitysite.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;
import park.mycommunitysite.filter.MyFilter1;

/**
 * WebSecurityConfigurerAdapter 은 스프링 시큐리티 5.7 버전 이상부터는 사용을 권장하지 않게
 * 됨으로써 SecurityFilterChain 빈을 사용하게 되었다.
 * 스프링 부트 3.0 부터는 스프링 시큐리티 6.1.0 이상을 사용한다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;// @RequiredArgsConstructor 에의하여 자동 주입되어 사용할 수 있다.

    /**
     * 시큐리티 필터 체인은 커스텀 필터 체인보다 우선적으로 동작한다.
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .addFilter(corsFilter)// @CrossOrigin(인증x), 시큐리티 필터에 등록 인증(O)
                .csrf(AbstractHttpConfigurer::disable) // csrf를 사용하지 않으므로 비활성화
                // httpBasic 비활성화 ( 기존 헤더에 Authorization 에 아이디와 비밀번호를 담아 요청하는 방식)
                // 해당방식은 매번 아이디와 비밀번호를 가지고 요청하는 방식이라 중간에 노출되면 문제가 된다.
                // 물론 위문제는 https 로 해결하기는 하나 우리가 쓸려는 방식은 Authorization에 토큰을 넣는 방식이라(Bearer 방식)
                // httpBasic을 비활성화한다.
                .httpBasic(AbstractHttpConfigurer::disable)// 기본 인증 방식 비활성화
                .formLogin(AbstractHttpConfigurer::disable)// 기존 폼로그인을 사용하지 않는다.
                // 세션을 stateless로 설정
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers(PathRequest.toH2Console()).permitAll() // h2 데이터에비스를 사용하기 위한 설정이다.
                                // ROLE_은 붙이면 안 된다. hasRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
                                .requestMatchers("/api/**").hasRole("USER") // 유저 권한이 있어야 사용가능하다.
                                .requestMatchers("/api/manager/**").hasRole("MANAGER") // 메니저 권한이 있어야 사용가능하다.
                                .anyRequest().permitAll()
                );


        return http.build();
    }
}
