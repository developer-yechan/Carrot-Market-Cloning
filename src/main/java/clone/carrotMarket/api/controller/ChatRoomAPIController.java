package clone.carrotMarket.api.controller;


import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.ChatRoomDTO;
import clone.carrotMarket.dto.EnterRoomDto;
import clone.carrotMarket.dto.SuccessDTO;
import clone.carrotMarket.service.ChatRoomService;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/chat")
@Log4j2
public class ChatRoomAPIController {
    private final ChatRoomService chatRoomService;

    //나의 채팅방 목록 조회
    @GetMapping("/rooms")
    public List<ChatRoomDTO> findMyRooms(@AuthenticationPrincipal PrincipalDetails principal){
        log.info("# All Chat Rooms");
        Member loginMember = principal.getMember();
        List<ChatRoomDTO> allRooms = chatRoomService.findAllRooms(loginMember.getId());
        return allRooms;
    }

    // 상품 관련 채팅방 목록 조회
    @GetMapping("/rooms/{sellId}")
    public List<ChatRoomDTO> findRoomsOfSell(@PathVariable Long sellId, @AuthenticationPrincipal PrincipalDetails principal,
                              Model model){
        Member loginMember = principal.getMember();
        List<ChatRoomDTO> roomsOfSell = chatRoomService.findRoomsOfSell(sellId);

        return roomsOfSell;
    }

    //채팅방 개설
    @PostMapping(value = "/rooms/{sellId}")
    public SuccessDTO create(@PathVariable Long sellId, @AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        Long senderId = loginMember.getId();
        log.info("# Create Chat Room , senderId: " + senderId);
        Long chatRoomId = chatRoomService.findRoomId(sellId, loginMember.getId());
        if(chatRoomId != null){
            return new SuccessDTO(302,"http://localhost:8080/api/chat/rooms/"+chatRoomId);
        }
        Long roomId = chatRoomService.createRoom(sellId, senderId);
        return new SuccessDTO(201,"http://localhost:8080/api/chat/rooms/"+roomId) ;
    }

    //채팅방 입장
    @GetMapping("/rooms/enter/{roomId}")
    public EnterRoomDto enterRoom(@PathVariable Long roomId , @AuthenticationPrincipal PrincipalDetails principal,
                          Model model, HttpServletResponse response) throws IOException {

        log.info("# Enter Chat Room, roomID : " + roomId);
        ChatRoomDTO chatRoomDTO = chatRoomService.findRoom(roomId);
        Member loginMember = principal.getMember();
        if(loginMember.getId() != chatRoomDTO.getSellerId() && loginMember.getId() != chatRoomDTO.getSenderId()){
            response.sendError(403, "잘못된 요청입니다.");
        }

        return new EnterRoomDto(chatRoomDTO,loginMember.getId(),loginMember.getNickname());
    }
}
