package clone.carrotMarket.api.controller;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Place;
import clone.carrotMarket.dto.*;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.service.MemberService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "Member", description = "회원가입, 조회, 수정 API")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입", description = "회원가입 한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse400.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/signup")
    public SuccessResponse saveMember(@Valid @RequestBody CreateMemberDto createMemberDto){

        Place memberPlace = new Place(createMemberDto.getPlace(), createMemberDto.getLatitude(), createMemberDto.getLongitude());
        String password = passwordEncoder.encode(createMemberDto.getPassword());
        Member member = new Member(createMemberDto.getEmail(), password,
                createMemberDto.getPhoneNumber(), createMemberDto.getNickname(), memberPlace);
        Long memberId = memberService.signUp(member);
        return new SuccessResponse(201,memberId.toString());
    }

    @Operation(summary = "마이페이지 사용자 정보 조회", description = "사용자 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(schema = @Schema(implementation = MyPageMemberDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse400.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public MyPageMemberDto findMember(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        Member member = memberRepository.findMemberById(loginMember.getId());
        return new MyPageMemberDto(member.getId(),member.getNickname(),member.getProfileImage());
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse400.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public SuccessResponse editMember(@Valid @RequestBody EditMemberDto editMemberDto) throws IOException {
            memberService.editMember(editMemberDto);
            return new SuccessResponse(200,"사용자 정보 수정 성공");
    }

    @Operation(summary = "프로필 사진 삭제", description = "프로필 사진을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 사진 삭제 완료", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse400.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name="memberId",description = "회원 id",in= ParameterIn.PATH ,required = true)
    })
    @PatchMapping("/{memberId}")
    public SuccessResponse deleteProfileImage(@PathVariable Long memberId){
        memberService.deleteProfileImage(memberId);
        return new SuccessResponse(200,"프로필 사진 삭제 완료");
    }
}
