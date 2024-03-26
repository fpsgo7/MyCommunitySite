package park.mycommunitysite.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity(name = "free_board_comment")
@Getter
public class FreeBoardComment extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "free_board_comment_id")
    private Long id;

    private String content;
    private int reported;
    private AdministratorPage administratorPage;
    private Member member;
    private FreeBoardPost freeBoardPost;
}
