package park.mycommunitysite.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import park.mycommunitysite.config.auth.CustomUserDetails;
import park.mycommunitysite.config.auth.CustomUserDetailsService;
import park.mycommunitysite.config.jwt.JwtAuthenticationFilter;
import park.mycommunitysite.config.jwt.JwtAuthorizationFilter;
import park.mycommunitysite.repository.MemberRepository;

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
    private final MemberRepository memberRepository;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * 시큐리티 필터 체인은 커스텀 필터 체인보다 우선적으로 동작한다.
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
        sharedObject.userDetailsService(this.customUserDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);
        http
                .addFilter(corsFilter)// @CrossOrigin(인증x), 시큐리티 필터에 등록 인증(O)
                .addFilter(new JwtAuthenticationFilter(authenticationManager)) // /formLogin이 비활성화 되어 대신 login 요청을 받기 위해 사용한다.
                .addFilter(new JwtAuthorizationFilter(authenticationManager,memberRepository)) // 토큰검증을 위해 사용한다.
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
                                // ROLE_은 붙이면 안 된다. hasRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
                                .requestMatchers("/api/**").hasRole("USER") // 유저 권한이 있어야 사용가능하다.
                                .requestMatchers("/api/manager/**").hasRole("MANAGER") // 메니저 권한이 있어야 사용가능하다.
                                .anyRequest().permitAll()
                );


        return http.build();
    }


}
