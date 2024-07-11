package clone.carrotMarket.dto;

import lombok.*;


@Getter
@ToString
@AllArgsConstructor
public class MyPageMemberDto {

    private Long id;
    private String nickname;

    private String profileImage;

}
