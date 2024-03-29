package park.mycommunitysite.domain;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "freeBoardPost")
    private List<FreeBoardComment> freeBoardComments;
}
