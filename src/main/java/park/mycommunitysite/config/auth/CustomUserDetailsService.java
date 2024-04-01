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
     * @param username the username identifying the user whose data is required.
     * @return 커스텀 유저 디테일즈 객체
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Member> members = memberRepository.findByEmail(username);

        return new CustomUserDetails(members.get(0));
    }
}
