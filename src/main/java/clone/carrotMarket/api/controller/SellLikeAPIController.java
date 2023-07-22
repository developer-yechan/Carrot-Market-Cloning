package clone.carrotMarket.api.controller;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.SellStatus;
import clone.carrotMarket.dto.SellDto;
import clone.carrotMarket.dto.SuccessDTO;
import clone.carrotMarket.service.SellLikeService;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/sells/like")
public class SellLikeAPIController {

    private final SellLikeService sellLikeService;

    @PostMapping("/{sellId}")
    public SuccessDTO addSellLike(@PathVariable Long sellId, @AuthenticationPrincipal PrincipalDetails principal ){
        Member loginMember = principal.getMember();
        sellLikeService.addSellLike(sellId, loginMember);
        return new SuccessDTO(201,"관심상품 등록 완료");
    }

    @DeleteMapping("/{sellId}")
    public SuccessDTO deleteSellLike(@PathVariable Long sellId, @AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        sellLikeService.deleteSellLike(sellId, loginMember);
        return new SuccessDTO(200,"관심상품 삭제 완료");
    }

    @GetMapping("/{memberId}")
    public List<SellDto> findLikeSells(@PathVariable Long memberId,
                                @RequestParam(defaultValue = "판매중") SellStatus sellStatus){
        List<SellDto> likeSells = sellLikeService.findLikeSells(memberId, sellStatus);
        return likeSells;
    }
}
