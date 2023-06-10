package clone.carrotMarket.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateMemberDto {

    @NotEmpty
    private String name;
    @NotEmpty @Email
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String nickname;

    @NotEmpty
    private String place;
    @NotNull
    private int latitude;
    @NotNull
    private int longitude;

}
