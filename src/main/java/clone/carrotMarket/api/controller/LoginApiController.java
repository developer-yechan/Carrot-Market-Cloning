package clone.carrotMarket.api.controller;
import clone.carrotMarket.dto.ErrorDTO;
import clone.carrotMarket.dto.LoginDto;
import clone.carrotMarket.dto.SuccessDTO;
import clone.carrotMarket.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Tag(name = "Login / Logout", description = "로그인 / 로그아웃 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginApiController {
    private final LoginService loginService;

    @Operation(summary = "login", description = "회원 임을 인증하고 토큰을 발급받는다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @PostMapping("/login")
    public SuccessDTO login(@RequestBody @Valid LoginDto loginDto){
        String accessToken = loginService.login(loginDto.getUsername(), loginDto.getPassword());
        return new SuccessDTO(200,accessToken);
    }

    @Operation(summary = "logout", description = "로그아웃한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
    })
    @PostMapping("/logout")
    public SuccessDTO logout(){
        loginService.logout();
        return new SuccessDTO(200,"로그아웃 성공");
    }
}
