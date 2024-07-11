package clone.carrotMarket.dto;

import clone.carrotMarket.domain.ImageRank;
import clone.carrotMarket.domain.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductImageDto {

    private String imageUrl;

    private ImageRank imageRank;

    public ProductImageDto(ProductImage productImage) {
        imageUrl = productImage.getImageUrl();
        imageRank = productImage.getImageRank();
    }
}
