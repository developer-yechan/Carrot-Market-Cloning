package clone.carrotMarket.dto;

import clone.carrotMarket.domain.Category;
import clone.carrotMarket.domain.ProductImage;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.file.FileStore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EditSellDto {

    private Long sellId;

    private List<ProductImageDto> productImages;

    private List <MultipartFile> imageFiles;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private int price;
    @NotNull
    private Category category;
    private String place;
    private int latitude;
    private int longitude;

    public EditSellDto(Sell sell){
        sellId = sell.getId();
        title = sell.getTitle();
        content = sell.getContent();
        productImages = sell.getProductImages().stream()
                .map(productImage -> new ProductImageDto(productImage))
                .collect(Collectors.toList());
        price = sell.getPrice();
        category = sell.getCategory();
        place = sell.getTradePlace().getPlace();
        latitude = sell.getTradePlace().getLatitude();
        longitude = sell.getTradePlace().getLongitude();

    }

    public EditSellDto(){

    }
}
