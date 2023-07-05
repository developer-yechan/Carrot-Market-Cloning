package clone.carrotMarket.web.security;
import clone.carrotMarket.dto.LoginDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtProperties jwtProperties;


    // login 요청을 하면 로그인 시도를 위해 실행되는 함수

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        ObjectMapper om = new ObjectMapper();
        try{
            LoginDto loginDto = om.readValue(request.getInputStream(), LoginDto.class);
            log.info(loginDto.toString());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            log.info("Authenticate Start");
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("Authenticate End");
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info(principalDetails.getMember().getEmail());
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // attemptAuthentication에서 인증이 완료되면 수행되는 메서드, JWT를 발급한다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("인증 완료");
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        String jwt = JWT.create()
                .withSubject("JWT_TOKEN")
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getValidSeconds())) //만료 시간
                .withClaim("id",principal.getMember().getId())
                .withClaim("username",principal.getMember().getEmail())
                .sign(Algorithm.HMAC512(jwtProperties.getSecretKey()));
        response.addHeader(jwtProperties.getJwtHeader(), "Bearer " +jwt);
    }
}
