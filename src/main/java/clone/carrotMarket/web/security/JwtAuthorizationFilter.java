package clone.carrotMarket.web.security;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.repository.MemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MemberRepository memberRepository;

    private JwtProperties jwtProperties;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository,JwtProperties jwtProperties) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtProperties = jwtProperties;
    }

    //인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 거친다.


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("request 헤더 검증");
        String jwtHeader = request.getHeader("Authorization");
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request,response);
            return;
        }
        String jwtToken = jwtHeader.replace("Bearer ", "");
        String username = null;
        try{
            log.info("jwt 검증");
            username = JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey()))
                    .build()
                    .verify(jwtToken)
                    .getClaim("username")
                    .asString();
            if(username != null){
                log.info("서명 검증 완료");
                List<Member> members = memberRepository.findByEmail(username);
                if(members.size()==0){
                    throw new IllegalStateException("존재하지 않는 유저입니다.");
                }
                PrincipalDetails principal = new PrincipalDetails(members.get(0));
                log.info("Authentication 객체 생성");
                Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                log.info("시큐리티 세션에 접근하여 Authentication 객체 저장");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("다음 필터로");
                chain.doFilter(request,response);
            }
        }catch (Exception e){
            throw new IllegalStateException("사용자 인증에 실패했습니다.");
        }

    }
}
