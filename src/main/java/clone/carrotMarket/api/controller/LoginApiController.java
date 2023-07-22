package clone.carrotMarket.api.controller;

import clone.carrotMarket.dto.LoginDto;
import clone.carrotMarket.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDto loginDto){
        String accessToken = loginService.login(loginDto.getUsername(), loginDto.getPassword());
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        loginService.logout();
        return ResponseEntity.ok("로그아웃 완료");
    }
}
