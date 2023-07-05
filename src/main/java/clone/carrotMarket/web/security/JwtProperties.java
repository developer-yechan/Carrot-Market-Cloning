package clone.carrotMarket.web.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {


    private final String secretKey;


    private final int validSeconds;


    private final String jwtHeader;

    public JwtProperties(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.token-validity-in-seconds}") int validSeconds,
                         @Value("${jwt.header}") String jwtHeader) {
        this.secretKey = secretKey;
        this.validSeconds = validSeconds;
        this.jwtHeader = jwtHeader;
    }
}
