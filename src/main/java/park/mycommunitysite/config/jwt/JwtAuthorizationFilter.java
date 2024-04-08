package park.mycommunitysite.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import park.mycommunitysite.config.auth.CustomUserDetails;
import park.mycommunitysite.domain.Member;
import park.mycommunitysite.repository.MemberRepository;

import java.io.IOException;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    /**
     * 어떤 요청이든 해당 필터를 타기 때문에
     * JWT 토큰을 검증하는 역할을한다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //super.doFilterInternal(request, response, chain); // 오버라이드 대상에서 이미 응답하는 문장이 있기에 비활성화 한다.
        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("JwtAuthorizationFilter : 헤더값 : "+jwtHeader);

        // 해당 요청이 비정상적일 경우나 , 로그인 중이 아닐경우 발생한다.
        // 해더로부터 JWT 토큰을 가져와 해당 JWT 토큰을 검증해서
        // 정상적인 사용자인지 확인한다.
        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        String token = jwtHeader.replace(JwtProperties.TOKEN_PREFIX,"");
        String email
                = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET)).build().verify(token).getClaim("email").asString();
        // 서명이 정상적으로 됬다면
        if(email!=null){
            Member member = memberRepository.findByEmail(email).get(0);

            CustomUserDetails customUserDetails = new CustomUserDetails(member);
            // 인증 객체를 임의로 만든다.(email이 null 이 아니기때문에 사용할 수 있다.)
            // 토큰 서명을 통해 정상이면 Authenticaion 객체를 만들어준다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장하였다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("JwtAuthorizationFilter : 서명이 완료되었습니다.");
        }
        chain.doFilter(request,response);
    }
}
