package clone.carrotMarket.dto;

import lombok.Data;

@Data
public class EnterRoomDto {
    private ChatRoomDTO chatRoomDTO;
    private  Long loginId;

    private String loginNickname;

    public EnterRoomDto(ChatRoomDTO chatRoomDTO,Long loginId, String loginNickname){
        this.chatRoomDTO = chatRoomDTO;
        this.loginId = loginId;
        this.loginNickname = loginNickname;
    }
}

