package clone.carrotMarket.service;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long signUp(Member member){
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicatedMember(Member member) {
        List<Member> members = memberRepository.findByEmail(member.getEmail());
        if(!members.isEmpty()){
            throw new IllegalStateException();
        }
    }

}
