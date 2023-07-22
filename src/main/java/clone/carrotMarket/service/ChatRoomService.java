package clone.carrotMarket.service;

import clone.carrotMarket.domain.ChatMessage;
import clone.carrotMarket.domain.ChatRoom;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.dto.ChatMessageDTO;
import clone.carrotMarket.dto.ChatRoomDTO;
import clone.carrotMarket.repository.ChatMessageRepository;
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

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final SellRepository sellRepository;


    //채팅방 생성
    @Transactional
    public Long createRoom(Long sellId, Long senderId) {
        try{
            Member sender = memberRepository.findById(senderId);
            List<Sell> sells  = sellRepository.findSellById(sellId);
            ChatRoom chatRoom = new ChatRoom(sender, sells.get(0));
            chatRoomRepository.save(chatRoom);
            return chatRoom.getId();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("잘못된 요청입니다.");
        }
    }

    public ChatRoomDTO findRoom(Long roomId) {
        try{
            List<ChatRoom> chatRooms = chatRoomRepository.findById(roomId);
            List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDTO(chatRoom))
                    .collect(Collectors.toList());
            return chatRoomDTOs.get(0);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("잘못된 요청입니다.");
        }
    }

    @Transactional
    public void saveChat(ChatMessageDTO ChatMessageDTO) {
        try{
            Member member = memberRepository.findById(ChatMessageDTO.getSenderId());
            List<ChatRoom> chatRooms = chatRoomRepository.findById(ChatMessageDTO.getRoomId());
            ChatMessage chatMessage = new ChatMessage(chatRooms.get(0),
                    member,ChatMessageDTO.getMessage());
            chatMessageRepository.save(chatMessage);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("잘못된 요청입니다.");
        }
    }

    public List<ChatRoomDTO> findAllRooms(Long memberId) {
        try{
            List<ChatRoom> chatRooms = chatRoomRepository.findAll(memberId);
            List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDTO(chatRoom))
                    .collect(Collectors.toList());
            return chatRoomDTOs;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("잘못된 요청입니다.");
        }
    }

    public List<ChatRoomDTO> findRoomsOfSell(Long sellId) {
        try{
            List<ChatRoom> chatRooms = chatRoomRepository.findRoomsOfSell(sellId);
            List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDTO(chatRoom))
                    .collect(Collectors.toList());
            return chatRoomDTOs;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("잘못된 요청입니다.");
        }

    }

    public Long findRoomId(Long sellId, Long senderId) {
        try{
            List<ChatRoom> chatrooms = chatRoomRepository.findRoomId(sellId, senderId);
            if(chatrooms.size()>0){
                List<Long> roomIds = chatrooms.stream().map(chatRoom -> chatRoom.getId()).collect(Collectors.toList());
                return roomIds.get(0);
            }
            return null;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("잘못된 요청입니다.");
        }

    }
}