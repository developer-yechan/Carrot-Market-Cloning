package clone.carrotMarket.dto;

import clone.carrotMarket.domain.ImageRank;
import clone.carrotMarket.domain.ProductImage;
import lombok.Data;

@Data
public class ProductImageDto {
    private String imageUrl;

    private ImageRank imageRank;

    public ProductImageDto(ProductImage productImage) {
        imageUrl = productImage.getImageUrl();
        imageRank = productImage.getImageRank();
    }
}
