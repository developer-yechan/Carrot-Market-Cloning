package clone.carrotMarket.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;
}
