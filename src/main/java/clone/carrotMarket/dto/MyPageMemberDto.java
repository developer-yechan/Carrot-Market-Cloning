package clone.carrotMarket.dto;

import lombok.Data;


@Data
public class MyPageMemberDto {

    private Long id;
    private String nickname;

    private String profileImage;

    public MyPageMemberDto(Long id, String nickname, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
