package clone.carrotMarket.service;

import clone.carrotMarket.domain.Member;
import clone.carrotMarket.domain.Sell;
import clone.carrotMarket.domain.SellLike;
import clone.carrotMarket.domain.SellStatus;
import clone.carrotMarket.dto.SellDto;
import clone.carrotMarket.repository.MemberRepository;
import clone.carrotMarket.repository.SellLikeRepository;
import clone.carrotMarket.repository.SellRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


    public List<SellDto>  findLikeSells(Long memberId, SellStatus sellStatus) {
        String filteringQuery = "";
        if(sellStatus != SellStatus.판매완료 && sellStatus != null){
            filteringQuery = "and s.sellStatus not in ('판매완료')";
        }else if(sellStatus == SellStatus.판매완료){
            filteringQuery = "and s.sellStatus in ('판매완료')";
        }
        List<SellLike> sellLikes = sellLikeRepository.findSellLikes(memberId,filteringQuery);

        return sellLikes.stream().map(sell -> new SellDto(sell))
                .collect(Collectors.toList());
    }
}
