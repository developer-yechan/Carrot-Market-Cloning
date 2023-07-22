package clone.carrotMarket.web.security;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private MemberRepository memberRepository;

    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private JwtTokenProvider jwtTokenProvider;

    private RedisTemplate<String,String> redisTemplate;

    public JwtAuthenticationFilter(AuthenticationManagerBuilder authenticationManagerBuilder,
                                   MemberRepository memberRepository,JwtTokenProvider jwtTokenProvider,
                                   RedisTemplate<String ,String> redisTemplate) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.redisTemplate = redisTemplate;
    }

    //인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 거친다.

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("request 헤더 검증");
        String jwtHeader = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request,response);
            return;
        }
        String jwtToken = jwtHeader.replace("Bearer ", "");
        String username = null;
            log.info("jwt 검증");
            username = jwtTokenProvider.validateToken(jwtToken);
            if(username != null){
                log.info("서명 검증 완료");
                List<Member> members = memberRepository.findByEmail(username);
                if(members.size()==0){
                    throw new IllegalStateException("존재하지 않는 유저입니다.");
                }
                String storedToken = redisTemplate.opsForValue().get(username);
                if(redisTemplate.hasKey(username) && storedToken != null){
                    PrincipalDetails principal = new PrincipalDetails(members.get(0));
                    log.info("Authentication 객체 생성");
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                    log.info("시큐리티 세션에 접근하여 Authentication 객체 저장");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("다음 필터로");
                }else{
                    throw new IllegalStateException("로그아웃 된 유저입니다.");
                }
                chain.doFilter(request,response);
            }else{
                throw new IllegalStateException("유효하지 않은 토큰입니다.");
            }
    }
}
