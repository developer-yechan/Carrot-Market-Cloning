package clone.carrotMarket.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class SellDetailPageDto {

    private Map<String,Object> sellDetailMap;
    private Long roomId;

    public SellDetailPageDto(Map<String, Object> sellDetailMap, Long roomId) {
        this.sellDetailMap = sellDetailMap;
        this.roomId = roomId;
    }
}
