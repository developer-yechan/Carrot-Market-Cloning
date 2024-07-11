package clone.carrotMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SuccessLoginResponse {

    private int code;
    private String message;
    private String accessToken;

}


