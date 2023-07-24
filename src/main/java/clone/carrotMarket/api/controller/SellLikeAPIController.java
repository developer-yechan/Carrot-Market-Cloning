package clone.carrotMarket.api.controller;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.SellStatus;
import clone.carrotMarket.dto.ErrorDTO;
import clone.carrotMarket.dto.SellDto;
import clone.carrotMarket.dto.SuccessDTO;
import clone.carrotMarket.service.SellLikeService;
import clone.carrotMarket.web.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sell Likes", description = "관심상품 등록, 조회, 삭제 API")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/sells/like")
public class SellLikeAPIController {

    private final SellLikeService sellLikeService;

    @Operation(summary = "관심상품 등록", description = "관심상품을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심상품 등록 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameter(name="sellId", description = "판매글 id")
    @PostMapping("/{sellId}")
    public SuccessDTO addSellLike(@PathVariable Long sellId, @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal ){
        Member loginMember = principal.getMember();
        sellLikeService.addSellLike(sellId, loginMember);
        return new SuccessDTO(201,"관심상품 등록 완료");
    }

    @Operation(summary = "관심상품 삭제", description = "관심상품을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심상품 삭제 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameter(name="sellId", description = "판매글 id")
    @DeleteMapping("/{sellId}")
    public SuccessDTO deleteSellLike(@PathVariable Long sellId, @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        sellLikeService.deleteSellLike(sellId, loginMember);
        return new SuccessDTO(200,"관심상품 삭제 완료");
    }

    @Operation(summary = "관심상품 목록 조회", description = "관심상품 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관심상품 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="memberId",description = "회원 id"),
            @Parameter(name="sellStatus",description = "판매 상태",example = "['판매중','판매완료']",in= ParameterIn.QUERY)
    })
    @GetMapping("/{memberId}")
    public List<SellDto> findLikeSells(@PathVariable Long memberId,
                                @RequestParam(defaultValue = "판매중") SellStatus sellStatus){
        List<SellDto> likeSells = sellLikeService.findLikeSells(memberId, sellStatus);
        return likeSells;
    }
}
