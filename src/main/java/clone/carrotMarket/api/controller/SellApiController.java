package clone.carrotMarket.api.controller;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.dto.*;
import clone.carrotMarket.service.ChatRoomService;
import clone.carrotMarket.service.SellService;
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
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "Sell", description = "판매글 생성, 조회, 수정, 삭제 API")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/sells")
public class SellApiController {

    private final SellService sellService;
    private final ChatRoomService chatRoomService;

    @Operation(summary = "나의 판매글 상세 조회", description = "나의 판매글을 상세 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "나의 판매글 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="sellId",description = "판매글 id",required = true),
            @Parameter(name="sellStatus",description = "판매 상태",example = "['판매중','판매완료']",in= ParameterIn.QUERY)
    })
    @GetMapping("/my/{sellId}")
    public SellDetailDto mySellDetail(@PathVariable Long sellId,
                                      @RequestParam(defaultValue = "판매중") SellStatus sellStatus){
        SellDetailDto mySell = sellService.findMySell(sellId);
        return mySell;
    }

    @Operation(summary = "나의 판매글 목록 조회", description = "나의 판매글 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 개설 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="sellStatus",description = "판매 상태",example = "['판매중','판매완료']",in= ParameterIn.QUERY)
    })
    @GetMapping("/my")
    public List<SellDto> findMySells(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal, @RequestParam(defaultValue = "판매중") SellStatus sellStatus){
        Member loginMember = principal.getMember();
        List<SellDto> mySells = sellService.findMySells(loginMember.getId(),sellStatus);
       return mySells;
    }

    @Operation(summary = "판매글 등록", description = "새로운 판매글을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "판매글 등록 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @PostMapping
    public SuccessDTO saveSell(@Valid @RequestBody CreateSellDto createSellDto,
                           @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal) throws IOException {

        Member loginMember = principal.getMember();
        Sell sell = sellService.save(createSellDto, loginMember);
        return new SuccessDTO(201,sell.getId().toString());
    }

    @Operation(summary = "판매 목록 조회", description = "판매 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @GetMapping
    public List<SellDto> findSells(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal){
        log.info("principal = {}",principal);
        Member loginMember = principal.getMember();
        List<SellDto> mySells = sellService.findSells(loginMember.getId());
        return mySells;
    }

    @Operation(summary = "판매글 상세 조회", description = "판매글을 상세 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매글 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="sellId",description = "판매글 id",required = true),
            @Parameter(name="sellerId",description = "판매자 id", in=ParameterIn.QUERY, required = true)
    })
    @GetMapping("/{sellId}")
    public SellDetailPageDto SellDetail(@PathVariable Long sellId, @RequestParam Long sellerId,
                                          @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        Map<String, Object> sellDetailMap = sellService.findSell(sellId, sellerId,loginMember);
        SellDetailDto sell = (SellDetailDto) sellDetailMap.get("sell");
        sell.getRoomIds().clear();
        Long roomId = chatRoomService.findRoomId(sellId, loginMember.getId());
        sellDetailMap.put("roomId",roomId);
        return new SellDetailPageDto(sellDetailMap,roomId);
    }

    @Operation(summary = "다른 판매 상품 목록 조회", description = "현재 상품 외 다른 판매 상품 목록 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "현재 상품 외 다른 판매 상품 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="sellId",description = "판매 글 id",required = true),
            @Parameter(name="memberId",description = "회원 id",in= ParameterIn.QUERY,required = true),
            @Parameter(name="sellStatus",description = "판매 상태",example = "['판매중','판매완료']",in= ParameterIn.QUERY)
    })
    @GetMapping("/other/{sellId}")
    public List<SellDto> findOtherSells(@PathVariable Long sellId,
                                 @RequestParam Long memberId,
                                 @RequestParam(required = false) SellStatus sellStatus){
        List<SellDto> sells = sellService.findOtherSells(sellId,memberId,sellStatus);
        return sells;
    }

    @Operation(summary = "판매 글 수정", description = "판매 글을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매 글 수정 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="sellId",description = "판매 글 id",required = true)
    })
    @PutMapping("/{sellId}")
    public SuccessDTO editSell(
            @Valid @RequestBody EditSellDto editSellDto, @PathVariable Long sellId) throws IOException {

        sellService.update(editSellDto);
        return new SuccessDTO(200,"판매 글 수정 완료");
    }

    @Operation(summary = "판매 상태 수정", description = "판매 상태를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매 상태 수정 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="sellId",description = "판매 글 id",required = true),
            @Parameter(name="sellStatus",description = "판매 상태",example = "['판매중','판매완료']",in= ParameterIn.QUERY,required = true)
    })
    @PatchMapping("/{sellId}")
    public SuccessDTO updateStatus(@PathVariable Long sellId, @RequestParam SellStatus sellStatus){
        sellService.updateStatus(sellId,sellStatus);
        return new SuccessDTO(200,"판매 상태 수정 완료");
    }

    @Operation(summary = "판매 글 삭제", description = "판매 글을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매 글 삭제 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @Parameters({
            @Parameter(name="sellId",description = "판매 글 id",required = true)
    })
    @DeleteMapping("/{sellId}")
    public SuccessDTO deleteSell(@PathVariable Long sellId){
        sellService.delete(sellId);
        return new SuccessDTO(200,"판매 글 삭제 완료");
    }
}
