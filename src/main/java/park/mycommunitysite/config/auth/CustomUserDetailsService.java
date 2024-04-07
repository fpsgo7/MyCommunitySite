package park.mycommunitysite.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import park.mycommunitysite.domain.Member;
import park.mycommunitysite.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    /**
     * 로그인 요청이 오면 실행된다.
     * 유저이름으로 이메일을 사용하기에 이메일로 작성한다.
     * @param email the email identifying the user whose data is required.
     * @return 커스텀 유저 디테일즈 객체
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Member> members = memberRepository.findByEmail(email);

        return new CustomUserDetails(members.get(0));
    }
}
