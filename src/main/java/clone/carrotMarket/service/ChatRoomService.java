package clone.carrotMarket.service;

import clone.carrotMarket.domain.ChatMessage;
import clone.carrotMarket.domain.ChatRoom;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.dto.ChatMessageDTO;
import clone.carrotMarket.dto.ChatRoomDTO;
import clone.carrotMarket.dto.ChatRoomDetailDTO;
import clone.carrotMarket.repository.ChatRoomRepository;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.repository.SellRepository;
import clone.carrotMarket.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import clone.carrotMarket.domain.Member;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    //    private Map<Long, ChatRoom> chatRooms;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final SellRepository sellRepository;

//    @PostConstruct
//    //의존관게 주입완료되면 실행되는 코드
//    private void init() {
//        chatRooms = new LinkedHashMap<>();
//    }

    //채팅방 불러오기
//    public List<ChatRoomDTO> findAllRoom() {
//        List<ChatRoom> chatrooms = chatRoomRepository.findAll();
//        return chatrooms.stream().map(chatRoom -> new ChatRoomDTO(chatRoom))
//                .collect(Collectors.toList());
//    }
//    public List<ChatRoom> findAllRoom() {
//        //채팅방 최근 생성 순으로 반환
//        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
//        Collections.reverse(result);
//
//        return result;
//    }

    // 채팅방 하나 불러오기
//    public ChatRoom findById(String roomId) {
//        return chatRooms.get(roomId);
//    }

    //채팅방 생성
    @Transactional
    public Long createRoom(Long sellId, Long senderId) {
        Member sender = memberRepository.findById(senderId);
        List<Sell> sells  = sellRepository.findSellById(sellId);
        ChatRoom chatRoom = new ChatRoom(sender, sells.get(0));
        chatRoomRepository.save(chatRoom);
        System.out.println("chatRoom = " + chatRoom);

        return chatRoom.getId();
    }

    public ChatRoomDTO findRoom(Long roomId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findById(roomId);
        List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDTO(chatRoom))
                .collect(Collectors.toList());
        return chatRoomDTOs.get(0);
    }

    @Transactional
    public void saveChat(ChatMessageDTO ChatMessageDTO) {
        Member member = memberRepository.findById(ChatMessageDTO.getSenderId());
        List<ChatRoom> chatRooms = chatRoomRepository.findById(ChatMessageDTO.getRoomId());
        ChatMessage chatMessage = new ChatMessage(chatRooms.get(0),
                member,ChatMessageDTO.getMessage());
        chatMessageRepository.save(chatMessage);
    }

//    public ChatRoom createRoom(String name) {
//        ChatRoom chatRoom = ChatRoom.create(name);
//        chatRooms.put(chatRoom.getRoomId(), chatRoom);
//        return chatRoom;
//    }
}