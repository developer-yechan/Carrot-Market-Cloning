package clone.carrotMarket.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class EnterRoomDto {
    private ChatRoomDto chatRoomDTO;
    private  Long loginId;
    private String loginNickname;

}

