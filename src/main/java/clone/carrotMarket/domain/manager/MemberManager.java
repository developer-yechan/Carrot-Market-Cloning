package clone.carrotMarket.domain.manager;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.EditMemberDto;
import clone.carrotMarket.file.LocalUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MemberManager {
    //    private final S3Upload s3Upload;
    private final LocalUpload localUpload;

    public void edit(Member member, EditMemberDto editMemberDto) throws IOException {
        if(editMemberDto.getImageFile() != null){
            if(StringUtils.hasText(editMemberDto.getImageFile().getOriginalFilename())){
                String storeFileName = localUpload.upload(editMemberDto.getImageFile());
                member.setProfileImage(storeFileName);
            }
        }
        member.setNickname(editMemberDto.getNickname());
    }

}
