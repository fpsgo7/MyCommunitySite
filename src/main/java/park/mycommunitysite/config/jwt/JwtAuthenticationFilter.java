package park.mycommunitysite.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import park.mycommunitysite.config.auth.CustomUserDetails;
import park.mycommunitysite.domain.Member;

import java.io.IOException;
import java.util.Date;

/**
 * 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있다.
 * /login 요청해서 email, password 전송하면(post)
 * UsernamePasswordAuthenticationFilter 가 인식하여 동작한다.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            // 1. email, password 을 받는다.
            // x-www-form-urlencoded 방식과, json 방식은 서로 같은 내용을 입력하여도
            // 서버가 받게되는 구조가 다르기때문에 주의해야한다.

            // 여기서는 json 데이터를 파싱하여 회원 객체를 생성한다.
            ObjectMapper objectMapper = new ObjectMapper();
            // 멤버 객체를 암호화되지 않은 비밀번호로 만들어 인증에 사용할 수 없다.
            Member member = objectMapper.readValue(request.getInputStream(),Member.class);
            // 그래서 위 멤버 객체를 토대로
            System.out.println("JwtAuthenticationFilter :"+ member);
            System.out.println("JwtAuthenticationFilter memberPassword:"+ member.getPassword());
            // 2. 유저 객체를 통하여 토큰을 생성한다.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getEmail(),member.getPassword());

            // 3. 토큰을 통해 로그인 시도를 한다.
            // 로그인 시도가 되면서 CustomUserDetailsService 의 loadUserByUsername() 함수가 실행된다.
            // 성공하면 그결과로 Authentication 객체가 만들어지며 authentication에는 내가 로그인한 정보가 담긴다.
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);
            // 4. 출력 확인용 로그인이 성공적이었다면 정보를 가져올 수 있다.
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            System.out.println("JwtAuthenticationFilter"+customUserDetails.getMember().getEmail());

            // 5. authentication 객체가 session 영역에 저장을 해야하고 그방법으로 return 해주면된다.
            // 리턴을 해주는 이유는 권환 관리를 security가 대신 하기 때문이다.
            // (세션 사용이유는 단지 권한 처리 때문이다.)
            return authentication;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;// 로그인 실패
    }

    /**
     * attemptAuthentication 실행후 인증이 정상적으로 되었으면 해당 함수가 실행된다.
     * 여기서 JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해준다.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 로그인 인증 성공");

        // Hash 암호화 방식으로 암호화 시킨다.
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String jwtToken = JWT.create()
                .withSubject("cosToken")
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))//현제시간 + 기간 시간 30분
                // withClaime 내가 원하는 값을 넣어 커스텀해도된다.
                .withClaim("id",customUserDetails.getMember().getId())
                .withClaim("email",customUserDetails.getMember().getEmail())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));
        // 해더를 통해 클라이언트 쪽으로 JWT 토큰이 응답된다.
        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX+jwtToken);
    }
}
