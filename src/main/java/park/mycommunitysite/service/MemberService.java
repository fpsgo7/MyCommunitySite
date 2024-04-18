package park.mycommunitysite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import park.mycommunitysite.domain.Member;
import park.mycommunitysite.form.JoinForm;
import park.mycommunitysite.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinMember(JoinForm joinForm){
        log.info("회원가입이 실행됩니다.");
        Member newMember = new Member(joinForm.getEmail()
                , bCryptPasswordEncoder.encode(joinForm.getPassword()));

        memberRepository.save(newMember);
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email).get(0);
    }
}
