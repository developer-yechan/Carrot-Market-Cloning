package clone.carrotMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse400 {
    private int code;
    private String message;

}
