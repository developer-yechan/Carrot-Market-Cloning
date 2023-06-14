package clone.carrotMarket.api.controller;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.dto.EditSellDto;
import clone.carrotMarket.dto.SellDetailDto;
import clone.carrotMarket.dto.SellDto;
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
    public SellDetailDto mySellDetail(@PathVariable Long sellId){
        List<Sell> mySells = sellRepository.findSellById(sellId);
        List<SellDetailDto> result = mySells.stream().map(mySell -> new SellDetailDto(mySell))
                .collect(Collectors.toList());
        return result.get(0);
    }

    //나의 판매글 목록페이지 Controller
    @GetMapping("/my")
    public List<SellDto> findMySells(@Login Member loginMember, @RequestParam(defaultValue = "판매중") SellStatus sellStatus){
        return sellService.findMySells(loginMember.getId(),sellStatus);
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
