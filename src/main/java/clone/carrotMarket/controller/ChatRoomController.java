package clone.carrotMarket.controller;


import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.ChatRoomDto;
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


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    //나의 채팅방 목록 조회
    @GetMapping("/rooms")
    public String rooms(Model model,@AuthenticationPrincipal PrincipalDetails principal){
        log.info("# All Chat Rooms");
        Member loginMember = principal.getMember();
        model.addAttribute("list",chatRoomService.findAllRooms(loginMember.getId()));
        model.addAttribute("loginId",loginMember.getId());
        return "chat/rooms";
    }

    // 상품 관련 채팅방 목록 조회
    @GetMapping("/rooms/{sellId}")
    public String roomsOfSell(@PathVariable Long sellId, @AuthenticationPrincipal PrincipalDetails principal,
                              Model model){
        Member loginMember = principal.getMember();
        model.addAttribute("list",chatRoomService.findRoomsOfSell(sellId));
        model.addAttribute("loginId",loginMember.getId());
        return "chat/rooms";
    }

    //채팅방 개설
    @PostMapping(value = "/room/{sellId}")
    public String create(@PathVariable Long sellId, @AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        Long senderId = loginMember.getId();
        log.info("# Create Chat Room , senderId: " + senderId);
        Long chatRoomId = chatRoomService.findRoomId(sellId, loginMember.getId());
        if(chatRoomId != null){
            return "redirect:/chat/room/"+chatRoomId;
        }
        Long roomId = chatRoomService.createRoom(sellId, senderId);
        return "redirect:/chat/room/"+roomId;
    }

    //채팅방 입장
    @GetMapping("/room/{roomId}")
    public String getRoom(@PathVariable Long roomId , @AuthenticationPrincipal PrincipalDetails principal,
                          Model model, HttpServletResponse response) throws IOException {

        log.info("# Enter Chat Room, roomID : " + roomId);
        ChatRoomDto chatRoomDTO = chatRoomService.findRoom(roomId);
        Member loginMember = principal.getMember();
        if(loginMember.getId() != chatRoomDTO.getSellerId() && loginMember.getId() != chatRoomDTO.getSenderId()){
            response.sendError(403, "잘못된 요청입니다.");
        }
        model.addAttribute("room", chatRoomDTO);
        model.addAttribute("loginMember",loginMember);
        return "chat/room";
    }
}
