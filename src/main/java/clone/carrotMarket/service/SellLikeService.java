package clone.carrotMarket.service;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.repository.SellLikeRepository;
import clone.carrotMarket.repository.SellRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SellLikeService {

    private final SellLikeRepository sellLikeRepository;
    private final SellRepository sellRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addSellLike(Long sellId, Member loginMember) {
        sellLikeRepository.save(sellId, loginMember);
    }

    @Transactional
    public void deleteSellLike(Long sellId, Member loginMember) {

        sellLikeRepository.delete(sellId,loginMember);
    }
}
