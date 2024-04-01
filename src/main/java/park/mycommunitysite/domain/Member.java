package park.mycommunitysite.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자 protected로 하여 함부로 객체생성을 못하게함
public class Member {

    @Id
    @GeneratedValue// (strategy = GenerationType.AUTO)<- 기본값이며 자동 증가를 지원한다.
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String roles;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<FreeBoardPost> freeBoardPosts;
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<FreeBoardComment> freeBoardComments;

    public  Member(Member member,String encryptedPassword){
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = encryptedPassword;
        this.roles = "USER";
    }

    /*--핵심 비즈니스 로직--*/

    // ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력 -> 그걸 파싱!!
    // 위 방식으로 하나의 회원에 여러 규칙을 적용할 수 잇다.
    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

}
