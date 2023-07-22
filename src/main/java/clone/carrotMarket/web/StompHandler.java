package clone.carrotMarket.web;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.web.security.JwtTokenProvider;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private RedisTemplate<String,String> redisTemplate;
    private MemberRepository memberRepository;

    // api로 채팅 구현했을 때 jwt 토큰 검증하는 메서드
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("message : {}", message);
        log.info("Header : {}", message.getHeaders());
        log.info("Token : {}", accessor.getNativeHeader("Authorization"));
        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            String username = jwtTokenProvider.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7));
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
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    throw new IllegalStateException("로그아웃 된 유저입니다.");
                }
            }else{
                throw new IllegalStateException("유효하지 않은 토큰입니다.");
            }
        }
        return message;
    }
}
