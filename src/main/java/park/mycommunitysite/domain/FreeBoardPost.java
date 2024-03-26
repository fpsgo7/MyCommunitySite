package park.mycommunitysite.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.List;

@Entity(name = "free_board_post")
@Getter
public class FreeBoardPost extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "free_board_post_id")
    private Long id;

    private String title;

    private String content;

    private Long view;

    private int reported;

    private int recommend;

    private AdministratorPage administratorPage;

    private Member member;

    private List<FreeBoardComment> freeBoardComments;
}
