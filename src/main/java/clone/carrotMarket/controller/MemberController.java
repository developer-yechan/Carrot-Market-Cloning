package clone.carrotMarket.controller;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Place;
import clone.carrotMarket.dto.CreateMemberDto;
import clone.carrotMarket.dto.EditMemberDto;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.service.LoginService;
import clone.carrotMarket.service.MemberService;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String newMemberForm(Model model){
        model.addAttribute("createMemberDto",new CreateMemberDto());
        return "members/createMemberForm";
    }

    @PostMapping("/signup")
    public String saveMember(@Valid @ModelAttribute CreateMemberDto createMemberDto,
                             BindingResult result, Model model){
        if(result.hasErrors()){
            return "members/createMemberForm";
        }
        Place memberPlace = new Place(createMemberDto.getPlace(), createMemberDto.getLatitude(), createMemberDto.getLongitude());
        String password = passwordEncoder.encode(createMemberDto.getPassword());
        Member member = new Member(createMemberDto.getEmail(), password,
                createMemberDto.getPhoneNumber(), createMemberDto.getNickname(), memberPlace);
        try {
            memberService.signUp(member);
        }catch(Exception e){
            result.reject("signUpError","이미 등록된 회원이 존재합니다.");
            return "members/createMemberForm";
        }

        return "redirect:/members/login";
    }

    @GetMapping("/members/myPage")
    public String myPage(@AuthenticationPrincipal PrincipalDetails principal, Model model){
        Member loginMember = principal.getMember();
        Member member = memberRepository.findMemberById(loginMember.getId());
        model.addAttribute("member",member);
        return "members/myPage";
    }


    @GetMapping("/members/edit")
    public String editMemberPage(@AuthenticationPrincipal PrincipalDetails principal, Model model){
        Member loginMember = principal.getMember();
        Member member = memberRepository.findMemberById(loginMember.getId());
        EditMemberDto editMemberDto = new EditMemberDto();
        editMemberDto.setId(member.getId());
        editMemberDto.setNickname(member.getNickname());
        editMemberDto.setProfileImage(member.getProfileImage());
        model.addAttribute("member",editMemberDto);
        return "members/editProfile";
    }

    @PatchMapping("/members/edit")
    public String editMember(@Valid @ModelAttribute EditMemberDto editMemberDto,
                             BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()){
            return "members/editProfile";
        }
        memberService.editMember(editMemberDto);
        return "redirect:/members/myPage";
    }

    @PatchMapping("/members/deleteImage/{memberId}")
    public String deleteProfileImage(@PathVariable Long memberId){
        memberService.deleteProfileImage(memberId);
        return "redirect:/members/myPage";
    }

}
