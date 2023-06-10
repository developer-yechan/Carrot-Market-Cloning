package clone.carrotMarket.dto;

import clone.carrotMarket.domain.Category;
import clone.carrotMarket.domain.ImageRank;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.domain.SellStatus;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MySellDto {
    private Long sellId;
    private String title;
    private List<ProductImageDto> productImages;

    private int sellLikeCnt;
    private int price;
    private SellStatus sellStatus;

    private int chatRoomCnt;

    private String userPlace;

    public MySellDto(Sell sell){
        sellId = sell.getId();
        title = sell.getTitle();
        productImages = sell.getProductImages().stream()
                .filter(productImage -> productImage.getImageRank().equals(ImageRank.대표))
                .map(productImage ->
                       new ProductImageDto(productImage))
                .collect(Collectors.toList());
        sellLikeCnt = sell.getSellLikes().size();
        price = sell.getPrice();
        chatRoomCnt = sell.getChatRooms().size();
        userPlace = sell.getMember().getMyPlace().getPlace();
    }

}


