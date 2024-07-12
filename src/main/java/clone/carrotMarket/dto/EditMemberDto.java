package clone.carrotMarket.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;


@Data
public class EditMemberDto {

    private Long id;

    @NotEmpty
    private String nickname;

    private MultipartFile imageFile;

    private String profileImage;

}
