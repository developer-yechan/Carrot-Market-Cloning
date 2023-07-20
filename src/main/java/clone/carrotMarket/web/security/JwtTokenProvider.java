package clone.carrotMarket.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {


    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final Long tokenValidTime = 300 * 60 * 1000L;

    @PostConstruct
    protected void init(){
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String generateToken(PrincipalDetails principal){
        String jwt = JWT.create()
                .withSubject("JWT_TOKEN")
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenValidTime)) //만료 시간
                .withClaim("id",principal.getMember().getId())
                .withClaim("username",principal.getMember().getEmail())
                .sign(Algorithm.HMAC512(SECRET_KEY));
        return jwt;
    }

    public String validateToken(String jwtToken){
        String username = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(jwtToken)
                .getClaim("username")
                .asString();
        return username;

    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }


}
