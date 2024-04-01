package park.mycommunitysite.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import park.mycommunitysite.domain.Member;

import java.util.List;

@Repository
@Transactional
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findById(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){
        return em.createQuery("SELECT m FROM member m", Member.class).getResultList();
    }

    public List<Member> findByEmail(String email){
        return em.createQuery("select m from Member m where m.email = :email"
                        , Member.class)// :name 에는 파라미터 값이 들어간다.
                .setParameter("email",email) // 파라미터 값 적용
                .getResultList();
    }

}
