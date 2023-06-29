package clone.carrotMarket.controller;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.SellStatus;
import clone.carrotMarket.dto.SellDto;
import clone.carrotMarket.service.SellLikeService;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/sells/like")
public class SellLikeController {

    private final SellLikeService sellLikeService;

    @PostMapping("/{sellId}")
    public String addSellLike(@PathVariable Long sellId, @AuthenticationPrincipal PrincipalDetails principal, @RequestParam Long sellerId ){
        Member loginMember = principal.getMember();
        if(loginMember == null){
            return "redirect:/members/login";
        }
        sellLikeService.addSellLike(sellId, loginMember);
        return "redirect:/sells/"+sellId+"?sellerId="+sellerId;
    }

    @DeleteMapping("/{sellId}")
    public String deleteSellLike(@PathVariable Long sellId, @AuthenticationPrincipal PrincipalDetails principal, @RequestParam Long sellerId ){
        Member loginMember = principal.getMember();
        if(loginMember == null){
            return "redirect:/members/login";
        }
        sellLikeService.deleteSellLike(sellId, loginMember);
        return "redirect:/sells/"+sellId+"?sellerId="+sellerId;
    }

    @GetMapping("/{memberId}")
    public String findLikeSells(@PathVariable Long memberId,
                                @RequestParam(defaultValue = "판매중") SellStatus sellStatus, Model model){
        List<SellDto> likeSells = sellLikeService.findLikeSells(memberId, sellStatus);
        model.addAttribute("sells",likeSells);
        return "sells/likeSells";
    }
}
