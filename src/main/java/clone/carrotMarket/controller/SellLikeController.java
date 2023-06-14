package clone.carrotMarket.controller;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.service.SellLikeService;
import clone.carrotMarket.web.argumentresolver.Login;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/sells/like")
public class SellLikeController {

    private final SellLikeService sellLikeService;

    @PostMapping("/{sellId}")
    public String addSellLike(@PathVariable Long sellId, @Login Member loginMember, @RequestParam Long sellerId ){
        if(loginMember == null){
            return "redirect:/members/login";
        }
        sellLikeService.addSellLike(sellId, loginMember);
        return "redirect:/sells/"+sellId+"?sellerId="+sellerId;
    }

    @DeleteMapping("/{sellId}")
    public String deleteSellLike(@PathVariable Long sellId, @Login Member loginMember, @RequestParam Long sellerId ){
        if(loginMember == null){
            return "redirect:/members/login";
        }
        sellLikeService.deleteSellLike(sellId, loginMember);
        return "redirect:/sells/"+sellId+"?sellerId="+sellerId;
    }
}
