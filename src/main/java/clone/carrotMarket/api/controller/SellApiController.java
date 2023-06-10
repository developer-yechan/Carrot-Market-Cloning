package clone.carrotMarket.api.controller;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Place;
import clone.carrotMarket.domain.ProductImage;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.dto.EditSellDto;
import clone.carrotMarket.dto.MySellDetailDto;
import clone.carrotMarket.dto.MySellDto;
import clone.carrotMarket.file.FileStore;
import clone.carrotMarket.repository.SellRepository;
import clone.carrotMarket.service.SellService;
import clone.carrotMarket.web.argumentresolver.Login;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sells")
public class SellApiController {
    private final FileStore fileStore;

    private final SellService sellService;
    private final SellRepository sellRepository;

    //나의 판매글 상세페이지 Controller
    @GetMapping("/my/{sellId}")
    public MySellDetailDto mySellDetail(@PathVariable Long sellId){
        List<Sell> mySells = sellRepository.findMySellById(sellId);
        List<MySellDetailDto> result = mySells.stream().map(mySell -> new MySellDetailDto(mySell))
                .collect(Collectors.toList());
        return result.get(0);
    }

    //나의 판매글 목록페이지 Controller
    @GetMapping("/my")
    public List<MySellDto> findMySells(@Login Member loginMember){
        return sellService.findMySells(loginMember.getId());
    }

    @PatchMapping("/edit")
    public String editSell(
            @Valid @RequestBody EditSellDto editSellDto,
            @Login Member loginMember) throws IOException {

//        if(loginMember == null){
//           throw new IllegalStateException("먼저 로그인을 해주세요");
//        }
        sellService.update(editSellDto);
        return "수정완료";
    }
}
