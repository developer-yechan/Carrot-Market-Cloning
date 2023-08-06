package clone.carrotMarket.api.controller;
import clone.carrotMarket.dto.*;
import clone.carrotMarket.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Login / Logout", description = "로그인 / 로그아웃 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginApiController {
    private final LoginService loginService;

    @Operation(summary = "login", description = "회원 임을 인증하고 토큰을 발급받는다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = SuccessLoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse400.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public SuccessLoginResponse login(@RequestBody @Valid LoginDto loginDto){
        String accessToken = loginService.login(loginDto.getUsername(), loginDto.getPassword());
        return new SuccessLoginResponse(200,"로그인 성공",accessToken);
    }

    @Operation(summary = "logout", description = "로그아웃한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/logout")
    public SuccessResponse logout(){
        loginService.logout();
        return new SuccessResponse(200,"로그아웃 성공");
    }
}
