package clone.carrotMarket.service;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.LoginDto;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.web.security.PrincipalDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LoginService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Member> members = memberRepository.findByEmail(username);
        if(members.size() == 0){
            throw new UsernameNotFoundException("등록된 사용자가 아닙니다.");
        }
        return new PrincipalDetails(members.get(0));
    }
}
