package clone.carrotMarket.service;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.EditMemberDto;
import clone.carrotMarket.file.LocalUpload;
import clone.carrotMarket.file.S3Upload;
import clone.carrotMarket.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
//    private final S3Upload s3Upload;
    private final LocalUpload localpload;

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
            if(editMemberDto.getImageFile() != null){
                if(StringUtils.hasText(editMemberDto.getImageFile().getOriginalFilename())){
                    String storeFileName = localpload.upload(editMemberDto.getImageFile());
                    member.setProfileImage(storeFileName);
                }
            }
            member.setNickname(editMemberDto.getNickname());
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
    @Transactional
    public void deleteProfileImage(Long memberId){
        try{
            Member member = memberRepository.findMemberById(memberId);
            if(member.getProfileImage() != null){
                member.setProfileImage(null);
            }else{
                throw new IllegalStateException("프로필 사진이 이미 삭제되었습니다.");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
