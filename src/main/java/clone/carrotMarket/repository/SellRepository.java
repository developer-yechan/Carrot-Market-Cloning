package clone.carrotMarket.repository;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.domain.SellStatus;
import clone.carrotMarket.dto.EditSellDto;
import clone.carrotMarket.dto.MySellDetailDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
@Transactional(readOnly = true)
public class SellRepository {
    private final EntityManager em;

    @Transactional
    public void save(Sell sell){
        em.persist(sell);
    }

    // 나의 판매 글 상세페이지 조회
    public List<Sell> findMySellById(Long sellId){
        String query = "select distinct s from Sell s " +
                "join fetch s.member m " +
                "left join fetch s.productImages pi where s.id=:sellId";
        return em.createQuery(query,Sell.class).setParameter("sellId",sellId).getResultList();
    }

    // 남의 판매 글 상세페이지 조회
    public List<Member> findSellById(Long memberId){
        String query = "select distinct m from Member m " +
                "join fetch m.sell s " +
                "where m.id=:memberId";
        return em.createQuery(query, Member.class).setParameter("memberId",memberId).getResultList();
    }

    // 나의 판매내역 페이지 조회
    public List<Sell> findMySellsByMemberId(Long memberId){
        String query = "select distinct s from Sell s " +
                "join fetch s.member m " +
                "left join fetch s.productImages pi where m.id=:memberId";
        return em.createQuery(query,Sell.class).setParameter("memberId",memberId).getResultList();
    }

    // 남의 판매내역 페이지 조회
    public List<Sell> findSellsByMemberId(Long memberId){
        String query = "select distinct s from Sell s " +
                "join fetch s.member m " +
                "left join fetch s.productImages pi where m.id>:memberId or m.id<:memberId";
        return em.createQuery(query,Sell.class).setParameter("memberId",memberId).getResultList();
    }



    public List<Sell> findMySimpleSellById(Long sellId){
        String query = "select distinct s from Sell s " +
                "left join fetch s.productImages pi where s.id=:sellId";
        return em.createQuery(query,Sell.class).setParameter("sellId",sellId).getResultList();
    }

    public void delete(Sell sell) {
      em.remove(sell);
    }

    public Sell updateStatus(Long sellId, SellStatus sellStatus) {
        Sell sell = em.find(Sell.class, sellId);
        sell.setSellStatus(sellStatus);
        return sell;
    }
}
