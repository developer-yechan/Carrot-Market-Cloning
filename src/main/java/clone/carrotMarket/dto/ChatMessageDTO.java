package clone.carrotMarket.dto;

import clone.carrotMarket.domain.ChatMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageDTO {
    private Long roomId;

    private Long senderId;

    //보내는 사람
    private String sender;
    //내용
    private String message;

    public  ChatMessageDTO(ChatMessage chatMessage) {
        roomId = chatMessage.getChatRoom().getId();
        senderId = chatMessage.getSender().getId();
        sender = chatMessage.getSender().getName();
        message = chatMessage.getMessage();
    }
}
