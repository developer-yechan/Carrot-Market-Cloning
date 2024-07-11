package clone.carrotMarket.dto;

import clone.carrotMarket.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class ChatRoomDto {
    private Long id;
    private Long sellerId;
    private Long senderId;
    private String sellerImage;

    private String sellerNickname;

    private Long sellId;

    private String sellTitle;
    private SellStatus sellStatus;
    private int price;
    private ProductImageDto productImage;

    private String senderImage;
    private String senderNickname;

    private List<ChatMessageDto> chatMessages;

    public ChatRoomDto(ChatRoom chatRoom) {
        id = chatRoom.getId();
        sellerId = chatRoom.getSell().getMember().getId();
        senderId = chatRoom.getSender().getId();
        if(StringUtils.hasText(chatRoom.getSell().getMember().getProfileImage())){
            sellerImage = chatRoom.getSell().getMember().getProfileImage();

        }
        sellerNickname = chatRoom.getSell().getMember().getNickname();
        sellId = chatRoom.getSell().getId();
        sellTitle = chatRoom.getSell().getTitle();
        sellStatus = chatRoom.getSell().getSellStatus();
        price = chatRoom.getSell().getPrice();
        if(!chatRoom.getSell().getProductImages().isEmpty()){
            ProductImageDto representProductImage = new ProductImageDto(chatRoom.getSell().getProductImages()
                    .stream().filter(image -> image.getImageRank() == ImageRank.대표)
                    .collect(Collectors.toList()).get(0));
            productImage = representProductImage;
        }
        if(StringUtils.hasText(chatRoom.getSender().getProfileImage())){
            senderImage = chatRoom.getSender().getProfileImage();
        }
        senderNickname = chatRoom.getSender().getNickname();
        chatMessages = chatRoom.getChatMessages().stream()
                .map(chatMessage -> new ChatMessageDto(chatMessage))
                .collect(Collectors.toList());
    }
}
