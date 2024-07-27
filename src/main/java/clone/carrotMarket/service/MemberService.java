package clone.carrotMarket.service;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.manager.MemberManager;
import clone.carrotMarket.dto.EditMemberDto;
import clone.carrotMarket.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberManager memberManager;


    @Transactional
    public Long signUp(Member member){
        try{
            validateDuplicatedMember(member);
            memberRepository.save(member);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return member.getId();
    }

    private void validateDuplicatedMember(Member member) {
        List<Member> members = memberRepository.findByEmail(member.getEmail());
        if(!members.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원 email입니다.");
        }
    }

    @Transactional
    public void editMember(EditMemberDto editMemberDto) throws IOException {
        try{
            Member member = memberRepository.findMemberById(editMemberDto.getId());
            memberManager.edit(member,editMemberDto);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
    @Transactional
    public void deleteProfileImage(Long memberId){
        try{
            Member member = memberRepository.findMemberById(memberId);
            member.removeProfileImage();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
