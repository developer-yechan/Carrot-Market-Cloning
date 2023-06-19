package clone.carrotMarket.dto;

import clone.carrotMarket.domain.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SellDetailDto {
    private Long sellId;
    private String title;
    private String content;
    private List<ProductImageDto> productImages;

    private int sellLikeCnt;
    private int price;

    private Category category;

    private Place tradePlace;

    private SellStatus sellStatus;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long memberId;
    private String memberNickname;

    private String memberImage;

    private String memberPlace;

    private List<Long> roomIds;

    public SellDetailDto(Sell sell){
        sellId = sell.getId();
        title = sell.getTitle();
        content = sell.getContent();
        productImages = sell.getProductImages().stream()
                .map(productImage -> new ProductImageDto(productImage))
                .collect(Collectors.toList());
        sellLikeCnt = sell.getSellLikes().size();
        price = sell.getPrice();
        category = sell.getCategory();
        tradePlace = sell.getTradePlace();
        sellStatus = sell.getSellStatus();
        views = sell.getViews();
        createdAt = sell.getCreatedAt();
        updatedAt = sell.getUpdatedAt();
        memberId = sell.getMember().getId();
        memberNickname = sell.getMember().getNickname();
        memberImage = sell.getMember().getProfileImage();
        memberPlace = sell.getMember().getMyPlace().getPlace();
        if(sell.getChatRooms().size()>0){
            roomIds = sell.getChatRooms().stream()
                    .map(chatRoom -> chatRoom.getId())
                    .collect(Collectors.toList());
        }
    }

}
