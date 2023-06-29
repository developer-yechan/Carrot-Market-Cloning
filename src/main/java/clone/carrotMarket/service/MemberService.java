package clone.carrotMarket.service;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.EditMemberDto;
import clone.carrotMarket.file.S3Upload;
import clone.carrotMarket.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Upload s3Upload;

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
    @Transactional
    public void editMember(EditMemberDto editMemberDto) throws IOException {
        Member member = memberRepository.findMemberById(editMemberDto.getId());
        if(StringUtils.hasText(editMemberDto.getImageFile().getOriginalFilename())){
            String storeFileName = s3Upload.upload(editMemberDto.getImageFile(),"profile");
            member.setProfileImage(storeFileName);
        }
      member.setNickname(editMemberDto.getNickname());
    }
    @Transactional
    public void deleteProfileImage(Long memberId){
        Member member = memberRepository.findMemberById(memberId);
        member.setProfileImage(null);
    }
}
