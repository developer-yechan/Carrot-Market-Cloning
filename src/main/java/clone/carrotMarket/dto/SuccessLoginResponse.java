package clone.carrotMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessLoginResponse {

    private int code;
    private String message;
    private String accessToken;

}


