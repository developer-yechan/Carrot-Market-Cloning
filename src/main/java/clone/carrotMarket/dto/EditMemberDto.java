package clone.carrotMarket.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;


@Data
@Setter(AccessLevel.NONE)
public class EditMemberDto {

    @Setter
    private Long id;

    @NotEmpty
    @Setter
    private String nickname;

    private MultipartFile imageFile;

    @Setter
    private String profileImage;

}
