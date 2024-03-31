package park.mycommunitysite.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    /**
     * 크로스 오리진 요청을 허가하는 필터이다.
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration =new CorsConfiguration();
        configuration.setAllowCredentials(true);// 내서버가 응답할때 json을 자바스크립트에서 처리할 수 있게 설정한다.
        configuration.addAllowedOrigin("*");// 모든 ip에 응답을 허용
        configuration.addAllowedHeader("*");// 모든 헤더에 응답을 허용
        configuration.addAllowedMethod("*");// 모든 post,get,put,delete,patch 요청을 허용한다.
        source.registerCorsConfiguration("/api/**", configuration);// 모든 api 경로는 configration을 따라야한다.
        return new CorsFilter(source);
    }
}
