package clone.carrotMarket.repository;

import clone.carrotMarket.domain.Sell;
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

    public List<Sell> findMySellById(Long sellId){
        String query = "select distinct s from Sell s " +
                "join fetch s.member m " +
                "join fetch s.productImages pi where s.id=:sellId";
        return em.createQuery(query,Sell.class).setParameter("sellId",sellId).getResultList();
    }
    public List<Sell> findMySimpleSellById(Long sellId){
        String query = "select distinct s from Sell s " +
                "join fetch s.productImages pi where s.id=:sellId";
        return em.createQuery(query,Sell.class).setParameter("sellId",sellId).getResultList();
    }
}
