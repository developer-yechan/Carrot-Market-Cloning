package clone.carrotMarket.api.controller;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Place;
import clone.carrotMarket.dto.CreateMemberDto;
import clone.carrotMarket.dto.EditMemberDto;
import clone.carrotMarket.dto.MyPageMemberDto;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.service.MemberService;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public Long saveMember(@Valid @RequestBody CreateMemberDto createMemberDto){

        Place memberPlace = new Place(createMemberDto.getPlace(), createMemberDto.getLatitude(), createMemberDto.getLongitude());
        String password = passwordEncoder.encode(createMemberDto.getPassword());
        Member member = new Member(createMemberDto.getEmail(), password,
                createMemberDto.getPhoneNumber(), createMemberDto.getNickname(), memberPlace);
        Long memberId = memberService.signUp(member);
        return memberId;
    }

    @GetMapping
    public MyPageMemberDto findMember(@AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        Member member = memberRepository.findMemberById(loginMember.getId());
        return new MyPageMemberDto(member.getId(),member.getNickname(),member.getProfileImage());
    }

    @PutMapping
    public String editMember(@Valid @RequestBody EditMemberDto editMemberDto) throws IOException {
            memberService.editMember(editMemberDto);
            return "회원 정보 수정 완료";
    }

    @PatchMapping("/{memberId}")
    public String deleteProfileImage(@PathVariable Long memberId){
        memberService.deleteProfileImage(memberId);
        return "프로필 사진 삭제 완료";
    }
}
