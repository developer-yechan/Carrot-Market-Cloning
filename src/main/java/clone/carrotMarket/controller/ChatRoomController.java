package clone.carrotMarket.controller;

import clone.carrotMarket.domain.ChatRoom;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.ChatRoomDTO;
import clone.carrotMarket.repository.ChatRoomRepository;
import clone.carrotMarket.service.ChatRoomService;
import clone.carrotMarket.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    //나의 채팅방 목록 조회
    @GetMapping("/rooms")
    public String rooms(Model model,@Login Member loginMember){
        log.info("# All Chat Rooms");
        model.addAttribute("list",chatRoomService.findAllRooms(loginMember.getId()));
        model.addAttribute("loginId",loginMember.getId());
        return "chat/rooms";
    }

    // 상품 관련 채팅방 목록 조회
    @GetMapping("/rooms/{sellId}")
    public String roomsOfSell(@PathVariable Long sellId, @Login Member loginMember,
                              Model model){
        model.addAttribute("list",chatRoomService.findRoomsOfSell(sellId));
        model.addAttribute("loginId",loginMember.getId());
        return "chat/rooms";
    }

    //채팅방 개설
    @PostMapping(value = "/room/{sellId}")
    public String create(@PathVariable Long sellId, @Login Member loginMember){
        Long senderId = loginMember.getId();
        log.info("# Create Chat Room , senderId: " + senderId);
        Long roomId = chatRoomService.createRoom(sellId, senderId);
        return "redirect:/chat/room/"+roomId;
    }

    //채팅방 조회
    @GetMapping("/room/{roomId}")
    public String getRoom(@PathVariable Long roomId , @Login Member loginMember, Model model){

        log.info("# get Chat Room, roomID : " + roomId);
        ChatRoomDTO chatRoomDTO = chatRoomService.findRoom(roomId);

        model.addAttribute("room", chatRoomDTO);
        model.addAttribute("loginMember",loginMember);
        return "chat/room";
    }
}
