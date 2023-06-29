package clone.carrotMarket.controller;

import clone.carrotMarket.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginHome(Model model){
        model.addAttribute("loginDto",new LoginDto());
        return "members/loginForm";
    }
}
