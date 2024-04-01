package park.mycommunitysite.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "free_board_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "freeBoardPost" ,cascade = CascadeType.ALL)
    private List<FreeBoardComment> freeBoardComments;
}
