package clone.carrotMarket.dto;

import clone.carrotMarket.domain.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SellDto {
    private Long sellId;
    private String title;
    private List<ProductImageDto> productImages;

    private int sellLikeCnt;
    private int price;
    private SellStatus sellStatus;

    private int chatRoomCnt;

    private Long memberId;
    private String memberPlace;

    public SellDto(Sell sell){
        sellId = sell.getId();
        title = sell.getTitle();
        productImages = sell.getProductImages().stream()
                .filter(productImage -> productImage.getImageRank().equals(ImageRank.대표))
                .map(productImage ->
                       new ProductImageDto(productImage))
                .collect(Collectors.toList());
        sellStatus = sell.getSellStatus();
        sellLikeCnt = sell.getSellLikes().size();
        price = sell.getPrice();
        chatRoomCnt = sell.getChatRooms().size();
        memberId = sell.getMember().getId();
        memberPlace = sell.getMember().getMyPlace().getPlace();
    }

    public SellDto(SellLike sellLike){
        sellId = sellLike.getSell().getId();
        title = sellLike.getSell().getTitle();
        productImages = sellLike.getSell().getProductImages().stream()
                .filter(productImage -> productImage.getImageRank().equals(ImageRank.대표))
                .map(productImage ->
                        new ProductImageDto(productImage))
                .collect(Collectors.toList());
        sellStatus = sellLike.getSell().getSellStatus();
        sellLikeCnt = sellLike.getSell().getSellLikes().size();
        price = sellLike.getSell().getPrice();
        chatRoomCnt = sellLike.getSell().getChatRooms().size();
        memberId = sellLike.getSell().getMember().getId();
        memberPlace = sellLike.getSell().getMember().getMyPlace().getPlace();
    }

}


