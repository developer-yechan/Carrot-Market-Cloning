package clone.carrotMarket.repository;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.domain.SellLike;
import clone.carrotMarket.domain.SellStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
@Transactional(readOnly = true)
public class SellRepository {
    private final EntityManager em;

    @Transactional
    public void save(Sell sell){
        em.persist(sell);
    }

    // 판매 글 상세페이지 조회
    public List<Sell> findSellById(Long sellId){
        String query = "select distinct s from Sell s " +
                "join fetch s.member m " +
                "left join fetch s.productImages pi where s.id=:sellId";
        return em.createQuery(query,Sell.class).setParameter("sellId",sellId).getResultList();
    }


    // 남의 판매 글 다른 판매 상품 조회
    public List<Sell> findOtherSells(Long sellId, Long sellerId, String filteringQuery, int maxResults){
        String query = "select distinct s from Sell s " +
                "join fetch s.member m " +
                "left join fetch s.productImages pi where s.id not in (:sellId) " +
                "and m.id = :sellerId " + filteringQuery;
        return em.createQuery(query,Sell.class)
                .setParameter("sellId", sellId)
                .setParameter("sellerId",sellerId)
                .setMaxResults(maxResults)
                .getResultList();
    }

    // 나의 판매내역 페이지 조회
    public List<Sell> findMySellsByMemberId(Long memberId, String filteringQuery){
        String query = "select distinct s from Sell s " +
                "join fetch s.member m " +
                "left join fetch s.productImages pi where m.id=:memberId" + filteringQuery;
        return em.createQuery(query,Sell.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    // 남의 판매내역 페이지 조회
    public List<Sell> findSells(Long memberId){
        String query = "select distinct s from Sell s " +
                "join fetch s.member m " +
                "left join fetch s.productImages pi where m.id not in (:memberId) and s.sellStatus not in ('판매완료')";
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
        try{
            Sell sell = em.find(Sell.class, sellId);
            sell.setSellStatus(sellStatus);
            return sell;
        }catch(Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("존재하지 않는 게시물입니다.");
        }

    }

}
