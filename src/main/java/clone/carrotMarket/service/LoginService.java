package clone.carrotMarket.service;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.LoginDto;
import clone.carrotMarket.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(LoginDto loginDto){
        List<Member> members = memberRepository.findByEmail(loginDto.getEmail());
        if(members.size() == 0 || !loginDto.getPassword().equals(loginDto.getPassword())){
            return null;
        }
        return members.get(0);
    }
}
