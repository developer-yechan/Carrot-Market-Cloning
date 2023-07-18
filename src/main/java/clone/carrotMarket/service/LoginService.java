package clone.carrotMarket.service;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.web.security.JwtTokenProvider;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String,String> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Member> members = memberRepository.findByEmail(username);
        if(members.size() == 0){
            throw new UsernameNotFoundException("등록된 사용자가 아닙니다.");
        }
        return new PrincipalDetails(members.get(0));
    }

    @Transactional
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal);
        // 로그아웃과 구분하기 위해 redis에 저장
        redisTemplate.opsForValue().set(username, token);
        return token;
    }

    @Transactional
    public void logout() {
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(redisTemplate.opsForValue().get(principal.getUsername()) != null){
            redisTemplate.delete(principal.getUsername());
        }
    }
}
