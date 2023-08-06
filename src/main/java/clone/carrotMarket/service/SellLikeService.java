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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SellLikeService {

    private final SellLikeRepository sellLikeRepository;

    @Transactional
    public void addSellLike(Long sellId, Member loginMember) {
        try{
            sellLikeRepository.save(sellId, loginMember);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void deleteSellLike(Long sellId, Member loginMember) {
        try{
            sellLikeRepository.delete(sellId,loginMember);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    public List<SellDto>  findLikeSells(Long memberId, SellStatus sellStatus) {
        String filteringQuery = "";
        if(sellStatus != SellStatus.판매완료 && sellStatus != null){
            filteringQuery = "and s.sellStatus not in ('판매완료')";
        }else if(sellStatus == SellStatus.판매완료){
            filteringQuery = "and s.sellStatus in ('판매완료')";
        }
        try{
            List<SellLike> sellLikes = sellLikeRepository.findSellLikes(memberId,filteringQuery);
            return sellLikes.stream().map(sell -> new SellDto(sell))
                    .collect(Collectors.toList());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
