package clone.carrotMarket.repository;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.CreateMemberDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
@AllArgsConstructor
@Transactional
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findById(Long memberId){
        return em.find(Member.class,memberId);
    }

    public List<Member> findByEmail(String email){
        return em.createQuery("select m from Member m where m.email=:email")
                .setParameter("email", email).getResultList();
    }
}
