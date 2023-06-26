package clone.carrotMarket.service;

import clone.carrotMarket.domain.ChatMessage;
import clone.carrotMarket.domain.ChatRoom;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.dto.ChatMessageDTO;
import clone.carrotMarket.dto.ChatRoomDTO;
import clone.carrotMarket.repository.ChatRoomRepository;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.repository.SellRepository;
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

    public List<ChatRoomDTO> findAllRooms(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll(memberId);
        List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDTO(chatRoom))
                .collect(Collectors.toList());
        return chatRoomDTOs;
    }

    public Object findRoomsOfSell(Long sellId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findRoomsOfSell(sellId);
        List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDTO(chatRoom))
                .collect(Collectors.toList());
        return chatRoomDTOs;
    }

    public Long findRoomId(Long sellId, Long senderId) {
        List<ChatRoom> chatrooms = chatRoomRepository.findRoomId(sellId, senderId);
        if(chatrooms.size()>0){
            List<Long> roomIds = chatrooms.stream().map(chatRoom -> chatRoom.getId()).collect(Collectors.toList());
            return roomIds.get(0);
        }
        return null;
    }
}