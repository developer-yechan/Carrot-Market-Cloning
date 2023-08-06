package clone.carrotMarket.repository;

import clone.carrotMarket.domain.ChatRoom;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import javax.persistence.EntityManager;

@Repository
@AllArgsConstructor
public class ChatRoomRepository {
    private final EntityManager em;


    public void save(ChatRoom chatRoom){
        em.persist(chatRoom);
    }

    public List<ChatRoom> findById(Long roomId) {
        String query = "select cr from ChatRoom cr " +
                "join fetch cr.sender sd " +
                "join fetch cr.sell s " +
                "left join fetch cr.chatMessages cm " +
                "where cr.id=:roomId";
        return em.createQuery(query,ChatRoom.class)
                .setParameter("roomId",roomId)
                .getResultList();
    }

    public List<ChatRoom> findAll(Long memberId) {
        String query = "select distinct cr from ChatRoom cr " +
                "join fetch cr.sender sd " +
                "join fetch cr.sell s " +
                "left join fetch cr.chatMessages cm " +
                "where s.member.id=:sellerId " +
                "or cr.sender.id=:senderId";
        return em.createQuery(query,ChatRoom.class)
                .setParameter("sellerId",memberId)
                .setParameter("senderId",memberId)
                .getResultList();

    }

    public List<ChatRoom> findRoomsOfSell(Long sellId) {
        String query = "select distinct cr from ChatRoom cr " +
                "join fetch cr.sender sd " +
                "join fetch cr.sell s " +
                "left join fetch cr.chatMessages cm " +
                "where s.id = :sellId";
        return em.createQuery(query,ChatRoom.class)
                .setParameter("sellId",sellId)
                .getResultList();
    }

    public List<ChatRoom> findRoomId(Long sellId, Long senderId) {
        String query = "select cr from ChatRoom cr " +
                "where cr.sender.id = :senderId " +
                "and cr.sell.id = :sellId";
        return em.createQuery(query,ChatRoom.class)
                .setParameter("senderId",senderId)
                .setParameter("sellId",sellId)
                .getResultList();
    }
}
