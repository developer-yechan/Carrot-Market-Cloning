package clone.carrotMarket.service;

import clone.carrotMarket.domain.ChatMessage;
import clone.carrotMarket.domain.ChatRoom;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.dto.ChatMessageDto;
import clone.carrotMarket.dto.ChatRoomDto;
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

    public ChatRoomDto findRoom(Long roomId) {
        try{
            List<ChatRoom> chatRooms = chatRoomRepository.findById(roomId);
            List<ChatRoomDto> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDto(chatRoom))
                    .collect(Collectors.toList());
            return chatRoomDTOs.get(0);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalStateException("잘못된 요청입니다.");
        }
    }

    @Transactional
    public void saveChat(ChatMessageDto ChatMessageDTO) {
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

    public List<ChatRoomDto> findAllRooms(Long memberId) {
        try{
            List<ChatRoom> chatRooms = chatRoomRepository.findAll(memberId);
            List<ChatRoomDto> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDto(chatRoom))
                    .collect(Collectors.toList());
            return chatRoomDTOs;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<ChatRoomDto> findRoomsOfSell(Long sellId) {
        try{
            List<ChatRoom> chatRooms = chatRoomRepository.findRoomsOfSell(sellId);
            List<ChatRoomDto> chatRoomDTOs = chatRooms.stream().map(chatRoom -> new ChatRoomDto(chatRoom))
                    .collect(Collectors.toList());
            return chatRoomDTOs;
        }catch (Exception e){
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }

    }
}