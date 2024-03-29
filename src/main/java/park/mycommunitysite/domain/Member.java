package park.mycommunitysite.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(name = "members")
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String roles;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<FreeBoardPost> freeBoardPosts;
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<FreeBoardComment> freeBoardComments;

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
