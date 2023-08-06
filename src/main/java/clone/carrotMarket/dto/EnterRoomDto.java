package clone.carrotMarket.dto;

import lombok.Data;

@Data
public class EnterRoomDto {
    private ChatRoomDto chatRoomDTO;
    private  Long loginId;

    private String loginNickname;

    public EnterRoomDto(ChatRoomDto chatRoomDTO, Long loginId, String loginNickname){
        this.chatRoomDTO = chatRoomDTO;
        this.loginId = loginId;
        this.loginNickname = loginNickname;
    }
}

