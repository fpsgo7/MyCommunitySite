package park.mycommunitysite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import park.mycommunitysite.config.auth.CustomUserDetails;
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
        // newMember이 가리키는 객체의 id값은 null 이지만
        // save 메서드에 의하여 영속화 됨으로써 Member에 설정한 db 규칙이 적용되
        // id 값이  알아서 입력된다.
        // System.out.println(newMember.getId()); // 여기서 값을 추출하면 null 이다.
        memberRepository.save(newMember);

        // System.out.println("MemberController : member :"+ newMember.getRoleList());
        return "test";
    }

    /**
     * 스프링 시큐리티 테스트용
     * 아무나 접근가능
     */
    @GetMapping("/all")
    public String noneUser(){
        System.out.println("MEmberController : 아무나 접근가능");
        return "test";
    }

    /**
     * 스프링 시큐리티 태슽용
     * 인증된 대상만 접근가능
     * @return
     */
    @GetMapping("/user")
    public String user(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println("MemberController authentication : " + customUserDetails.getUsername());
        return "test";
    }

    /**
     * manager 만 접근가능
     * @return
     */
    @GetMapping("/manager")
    public String manager(){
        return "test";
    }
}
