package park.mycommunitysite.domain;

import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.List;

@Entity(name = "administrator_page")
@Getter
public class AdministratorPage {

    List<Member> Members;
    List<FreeBoardComment> freeBoardComments;
    List<FreeBoardPost> freeBoardPosts;

}
