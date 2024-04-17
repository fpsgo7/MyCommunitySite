package park.mycommunitysite.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 생성값 입력을 받기위한
 * Form 이다.
 */
@Getter
@Setter
public class JoinForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    @Size(min = 3, max = 25)
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

    @Size(min = 3, max = 25)
    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String passwordCheck;
}
