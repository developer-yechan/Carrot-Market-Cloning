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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
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
                                      @RequestParam(defaultValue = "판매중") SellStatus sellStatus){
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

    @PostMapping
    public Long saveSell(@Valid @RequestBody CreateSellDto createSellDto,
                           @AuthenticationPrincipal PrincipalDetails principal, RedirectAttributes redirectAttributes) throws IOException {

        Member loginMember = principal.getMember();
        Sell sell = sellService.save(createSellDto, loginMember);
        return sell.getId();
    }

    // 남의 판매 목록 페이지 Controller
    @GetMapping
    public List<SellDto> findSells(@AuthenticationPrincipal PrincipalDetails principal, Model model){
        log.info("principal = {}",principal);
        Member loginMember = principal.getMember();
        List<SellDto> mySells = sellService.findSells(loginMember.getId());
        return mySells;
    }

    // 남의 판매글 상세 페이지 API Controller
    @GetMapping("/{sellId}")
    public Map<String, Object> SellDetail(@PathVariable Long sellId, @RequestParam Long sellerId,
                             @AuthenticationPrincipal PrincipalDetails principal, Model model){
        Member loginMember = principal.getMember();
        Map<String, Object> sellDetailMap = sellService.findSell(sellId, sellerId,loginMember);
        SellDetailDto sell = (SellDetailDto) sellDetailMap.get("sell");
        sell.getRoomIds().clear();
        Long roomId = chatRoomService.findRoomId(sellId, loginMember.getId());
        sellDetailMap.put("roomId",roomId);
        return sellDetailMap;
    }

    // 다른 판매 상품 목록 페이지
    @GetMapping("/other/{sellId}")
    public List<SellDto> findOtherSells(@PathVariable Long sellId,
                                 @RequestParam Long memberId,
                                 @RequestParam(required = false) SellStatus sellStatus, Model model){
        List<SellDto> sells = sellService.findOtherSells(sellId,memberId,sellStatus);
        return sells;
    }

    @PutMapping("/{sellId}")
    public String editSell(
            @Valid @RequestBody EditSellDto editSellDto, @PathVariable Long sellId) throws IOException {

        sellService.update(editSellDto);
        return "판매 글 수정 완료";
    }

    @PatchMapping("/{sellId}")
    public String updateStatus(@PathVariable Long sellId, @RequestParam SellStatus sellStatus, HttpServletRequest request){
        sellService.updateStatus(sellId,sellStatus);
        return "판매 상태 수정 완료";
    }

    @DeleteMapping("/{sellId}")
    public String deleteSell(@PathVariable Long sellId){
        sellService.delete(sellId);
        return "판매 글 삭제 완료";
    }
}
