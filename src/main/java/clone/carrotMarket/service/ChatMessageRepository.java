package clone.carrotMarket.service;

import clone.carrotMarket.domain.ChatMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@AllArgsConstructor
public class ChatMessageRepository {

    private final EntityManager em;

    public void save(ChatMessage chatMessage){
        em.persist(chatMessage);
    }
}
