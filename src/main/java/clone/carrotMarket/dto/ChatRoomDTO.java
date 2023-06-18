package clone.carrotMarket.dto;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.repository.ChatRoomRepository;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ChatRoomDTO {
    private Long id;
    private Long sellerId;
    private Long senderId;
    private String sellerImage;

    private String sellerNickname;
    private SellStatus sellStatus;
    private int price;
    private ProductImageDto productImage;

    private String senderImage;
    private String senderNickname;

    private List<ChatMessage> chatMessages;

    public ChatRoomDTO(ChatRoom chatRoom) {
        id = chatRoom.getId();
        sellerId = chatRoom.getSell().getMember().getId();
        senderId = chatRoom.getSender().getId();
        if(StringUtils.hasText(chatRoom.getSell().getMember().getProfileImage())){
            sellerImage = chatRoom.getSell().getMember().getProfileImage();

        }
        sellerNickname = chatRoom.getSell().getMember().getNickname();
        sellStatus = chatRoom.getSell().getSellStatus();
        price = chatRoom.getSell().getPrice();
        if(chatRoom.getSell().getProductImages().size()>0){
            ProductImageDto representProductImage = new ProductImageDto(chatRoom.getSell().getProductImages()
                    .stream().filter(image -> image.getImageRank() == ImageRank.대표)
                    .collect(Collectors.toList()).get(0));
            productImage = representProductImage;
        }
        if(StringUtils.hasText(chatRoom.getSender().getProfileImage())){
            senderImage = chatRoom.getSender().getProfileImage();
        }
        senderNickname = chatRoom.getSender().getNickname();
        if(chatRoom.getChatMessages().size()>0){
            chatMessages = chatRoom.getChatMessages();
        }
    }
}

//public class ChatRoomDTO {
//    private Long roomId;
//    private String name;
////    private Set<WebSocketSession> sessions = new HashSet<>();
//    //WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션
//
//    public static ChatRoomDTO create(String name,Long sequence){
//        System.out.println("name = " + name);
//        ChatRoomDTO room = new ChatRoomDTO();
//
//        room.roomId = sequence;
//        room.name = name;
//        return room;
//    }
//}