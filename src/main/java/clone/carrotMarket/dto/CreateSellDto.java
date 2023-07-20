package clone.carrotMarket.dto;

import clone.carrotMarket.domain.Category;
import clone.carrotMarket.domain.Place;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateSellDto {

    private List<MultipartFile> imageFiles;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private int price;
    private Category category;
    private String place;
    private int latitude;
    private int longitude;
}
