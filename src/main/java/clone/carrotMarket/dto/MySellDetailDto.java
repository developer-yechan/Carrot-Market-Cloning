package clone.carrotMarket.dto;

import clone.carrotMarket.domain.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MySellDetailDto {
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

    private String userNickname;

    private String userImage;


    public MySellDetailDto(Sell sell){
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
        userNickname = sell.getMember().getNickname();
        userImage = sell.getMember().getProfileImage();
    }

}
