package clone.carrotMarket.api.controller;

import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.dto.MySellDetailDto;
import clone.carrotMarket.repository.SellRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sells")
public class SellApiController {
    private final SellRepository sellRepository;
    @GetMapping("/my/{sellId}")
    public MySellDetailDto mySellDetail(@PathVariable Long sellId){
        List<Sell> mySells = sellRepository.findMySellById(sellId);
        List<MySellDetailDto> result = mySells.stream().map(mySell -> new MySellDetailDto(mySell))
                .collect(Collectors.toList());
        return result.get(0);
    }
}
