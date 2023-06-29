package clone.carrotMarket.controller;

import clone.carrotMarket.dto.ChatMessageDTO;
import clone.carrotMarket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message) {
        chatRoomService.saveChat(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}