package park.mycommunitysite.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import park.mycommunitysite.config.auth.CustomUserDetails;
import park.mycommunitysite.form.JoinForm;
import park.mycommunitysite.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /* 회원가입 */
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm
            ,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "user/join";
        }
        if(!joinForm.getPassword().equals(joinForm.getPasswordCheck())){
            bindingResult.rejectValue("passwordCheck", "passwordInCorrect",
                    "2개의 비밀번호가 다릅니다. ");
            return "user/join";
        }

        try{
            memberService.findByEmail(joinForm.getEmail());
            bindingResult.rejectValue("email", "sameEmail",
                    "이미 존재한는 이메일입니다. ");
            return "user/join";
        }catch (Exception e){
            memberService.joinMember(joinForm);
            return "redirect:" + "/login";// 로그인 http 요청을 다시해라
        }
    }

    @GetMapping("/join")
    public String joinView(
            @ModelAttribute("joinForm")
            JoinForm joinForm
            ){
        return "user/join";
    }

    @GetMapping("/joinPopup")
    public String joinPopupView(){
        return "user/join_popup";
    }

    /* 로그인 */
    @GetMapping("/login")
    public String loginView(){
        return "user/login";
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
