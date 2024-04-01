package park.mycommunitysite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import park.mycommunitysite.domain.Member;
import park.mycommunitysite.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;

    @PostMapping("/join")
    public String join(@RequestBody Member member){
        Member newMember = new Member(member, bCryptPasswordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return "회원가입완료";
    }
}
