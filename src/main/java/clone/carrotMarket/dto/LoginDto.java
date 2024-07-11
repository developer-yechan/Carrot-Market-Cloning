package clone.carrotMarket.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@ToString
public class LoginDto {
    @NotEmpty
    @Email
    private String username; //사용자 email
    @NotEmpty
    private String password;
}
