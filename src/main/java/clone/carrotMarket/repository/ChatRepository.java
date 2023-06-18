package clone.carrotMarket.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@AllArgsConstructor
public class ChatRepository {
    private final EntityManager em;
}
