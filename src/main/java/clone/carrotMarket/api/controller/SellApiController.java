package clone.carrotMarket.api.controller;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.dto.CreateSellDto;
import clone.carrotMarket.dto.EditSellDto;
import clone.carrotMarket.dto.SellDetailDto;
import clone.carrotMarket.dto.SellDto;
import clone.carrotMarket.repository.SellRepository;
import clone.carrotMarket.service.ChatRoomService;
import clone.carrotMarket.service.SellService;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/sells")
public class SellApiController {

    private final SellService sellService;
    private final ChatRoomService chatRoomService;


    //나의 판매글 상세 API Controller
    @GetMapping("/my/{sellId}")
    public SellDetailDto mySellDetail(@PathVariable Long sellId,
                                      @AuthenticationPrincipal PrincipalDetails principal
    ,@RequestParam(defaultValue = "판매중") SellStatus sellStatus){
        if(principal == null){
            throw new IllegalStateException("로그인을 먼저 해주세요!");
        }
        log.info("인증된 사용자");
        SellDetailDto mySell = sellService.findMySell(sellId);
        return mySell;
    }

    //나의 판매글 목록 API Controller
    @GetMapping("/my")
    public List<SellDto> findMySells(@AuthenticationPrincipal PrincipalDetails principal, @RequestParam(defaultValue = "판매중") SellStatus sellStatus, Model model){
        Member loginMember = principal.getMember();
        List<SellDto> mySells = sellService.findMySells(loginMember.getId(),sellStatus);
       return mySells;
    }

    @PostMapping("/add")
    public String saveSell(@Valid @RequestBody CreateSellDto createSellDto, BindingResult result,
                           @AuthenticationPrincipal PrincipalDetails principal, RedirectAttributes redirectAttributes) throws IOException {
        if(result.hasErrors()){
            return "sells/addForm";
        }
        Member loginMember = principal.getMember();
        if(loginMember == null){
            return "redirect:/members/login";
        }

        Sell sell = sellService.save(createSellDto, loginMember);
        return "redirect:/sells/my/"+sell.getId();
    }

    // 남의 판매 목록 페이지 Controller
    @GetMapping
    public String findSells(@AuthenticationPrincipal PrincipalDetails principal, Model model){
        Member loginMember = principal.getMember();
        List<SellDto> mySells = sellService.findSells(loginMember.getId());
        model.addAttribute("myPlace",loginMember.getMyPlace().getPlace());
        model.addAttribute("sells",mySells);
        return "sells/sellList";
    }

    // 남의 판매글 상세 페이지 API Controller
    @GetMapping("/{sellId}")
    public String SellDetail(@PathVariable Long sellId, @RequestParam Long sellerId,
                             @AuthenticationPrincipal PrincipalDetails principal, Model model){
        Member loginMember = principal.getMember();
        Map<String, Object> sellDetailMap = sellService.findSell(sellId, sellerId,loginMember);
        Long roomId = chatRoomService.findRoomId(sellId, loginMember.getId());
        model.addAttribute("sell", sellDetailMap.get("sell"));
        model.addAttribute("otherSells", sellDetailMap.get("otherSells"));
        model.addAttribute("loginId",loginMember.getId());
        model.addAttribute("roomId",roomId);
        if(sellDetailMap.get("sellLike") != null){
            model.addAttribute("sellLikeBoolean",true);
        }else{
            model.addAttribute("sellLikeBoolean",false);
        }
        return "sells/sellDetail";
    }

    @PatchMapping("/edit")
    public String editSell(
            @Valid @RequestBody EditSellDto editSellDto,
            @AuthenticationPrincipal PrincipalDetails principal) throws IOException {

        if(principal.getMember().getId() == null){
           throw new IllegalStateException("먼저 로그인을 해주세요");
        }
        sellService.update(editSellDto);
        return "수정완료";
    }
}
