package clone.carrotMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


@Getter
@ToString
@AllArgsConstructor
public class SellDetailPageDto {

    private Map<String,Object> sellDetailMap;
    private Long roomId;

}
