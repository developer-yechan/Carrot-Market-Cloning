package clone.carrotMarket.repository;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.domain.SellLike;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
public class SellLikeRepository {
    private final EntityManager em;

    public void save(Long sellId, Member loginMember) {
        Sell sell = em.find(Sell.class, sellId);
        SellLike sellLike = new SellLike(loginMember, sell);
        em.persist(sellLike);

    }

    public void delete(Long sellId, Member loginMember) {
        String query = "select sl from SellLike sl " +
                "where sl.sell.id = :sellId " +
                "and sl.member = :member";

        List<SellLike> sellLikes = em.createQuery(query, SellLike.class)
                .setParameter("sellId", sellId)
                .setParameter("member", loginMember)
                .getResultList();

        em.remove(sellLikes.get(0));
    }

    public SellLike findLike(Long sellId, Long memberId) {
        String query = "select sl from SellLike sl " +
                "where sl.sell.id =:sellId " +
                "and sl.member.id = :memberId";
        List<SellLike> sellLikes = em.createQuery(query, SellLike.class)
                .setParameter("sellId", sellId)
                .setParameter("memberId", memberId)
                .getResultList();

        if(sellLikes.size()==0){
            return null;
        }
        return sellLikes.get(0);
    }
}
