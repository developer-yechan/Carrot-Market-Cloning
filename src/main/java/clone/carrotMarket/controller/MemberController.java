package clone.carrotMarket.controller;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Place;
import clone.carrotMarket.dto.CreateMemberDto;
import clone.carrotMarket.dto.LoginDto;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.service.LoginService;
import clone.carrotMarket.service.MemberService;
import clone.carrotMarket.web.SessionConst;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final MemberRepository memberRepository;

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
        Place userPlace = new Place(createMemberDto.getPlace(), createMemberDto.getLatitude(), createMemberDto.getLongitude());
        Member member = new Member(createMemberDto.getEmail(), createMemberDto.getPassword(),
                createMemberDto.getPhoneNumber(), createMemberDto.getNickname(),userPlace);
        try {
            memberService.signUp(member);
        }catch(Exception e){
            result.reject("signUpError","이미 등록된 회원이 존재합니다.");
            return "members/createMemberForm";
        }

        return "redirect:/members/login";
    }

    @GetMapping("/login")
    public String loginHome(Model model){
        model.addAttribute("loginDto",new LoginDto());
        return "members/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginDto loginDto, BindingResult result,
                        HttpServletRequest request, @RequestParam(defaultValue = "/sells") String redirectURL){
        if(result.hasErrors()){
            return "members/loginForm";
        }
        Member loginMember = loginService.login(loginDto);
        if(loginMember == null){
            result.reject("loginFail","아이디 또는 비밀번호가 틀렸습니다.");
            return "members/loginForm";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);
        return "redirect:"+redirectURL;
    }

}
