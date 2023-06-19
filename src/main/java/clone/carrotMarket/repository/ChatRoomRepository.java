package clone.carrotMarket.repository;

import clone.carrotMarket.domain.ChatRoom;
import clone.carrotMarket.dto.ChatRoomDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

@Repository
@AllArgsConstructor
public class ChatRoomRepository {

//    private Map<Long, ChatRoomDTO> chatRoomDTOMap;
//
//    private static long sequence = 0L;

    private final EntityManager em;


//    @PostConstruct
//    private void init(){
//        chatRoomDTOMap = new LinkedHashMap<>();
//    }
//
//    public List<ChatRoomDTO> findAllRooms(){
//        //채팅방 생성 순서 최근 순으로 반환
//        List<ChatRoomDTO> result = new ArrayList<>(chatRoomDTOMap.values());
//        Collections.reverse(result);
//
//        return result;
//    }
//
//    public ChatRoomDTO findRoomById(Long id){
//        System.out.println("chatRoomDtoMap" + chatRoomDTOMap);
//        return chatRoomDTOMap.get(id);
//    }

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
//    public ChatRoomDTO createChatRoomDTO(String name){
//        ++sequence;
//        ChatRoomDTO room = ChatRoomDTO.create(name,sequence);
//        System.out.println("room = " + room);
//        System.out.println("room.getRoomId() = " + room.getRoomId());
//        chatRoomDTOMap.put(room.getRoomId(), room);
//
//        return room;
//    }

//    public List<ChatRoom> findAll() {
//    }
//
//    public List<ChatRoom> findById(Long roomId) {
//    }
}
