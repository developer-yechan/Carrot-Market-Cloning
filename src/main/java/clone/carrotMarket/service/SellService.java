package clone.carrotMarket.service;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.dto.*;
import clone.carrotMarket.file.FileStore;
import clone.carrotMarket.repository.SellLikeRepository;
import clone.carrotMarket.repository.SellRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class SellService {
    private final SellRepository sellRepository;

    private final SellLikeRepository sellLikeRepository;

    private final FileStore fileStore;

    @Transactional
    public Long update(EditSellDto editSellDto) throws IOException {
        List<Sell> mySells = sellRepository.findMySimpleSellById(editSellDto.getSellId());
        Sell mySell = mySells.get(0);
        if(editSellDto.getImageFiles() != null){
            List<ProductImage> productImages = fileStore.storeImages(editSellDto.getImageFiles());
            mySell.getProductImages().clear();
            for (ProductImage productImage : productImages) {
                mySell.addProductImage(productImage);
            }
        }
        if(StringUtils.hasText(editSellDto.getPlace())){
            Place place = new Place(editSellDto.getPlace(), editSellDto.getLatitude(), editSellDto.getLongitude());
            mySell.setTradePlace(place);
        }
        mySell.setTitle(editSellDto.getTitle());
        mySell.setContent(editSellDto.getContent());
        mySell.setPrice(editSellDto.getPrice());
        mySell.setCategory(editSellDto.getCategory());

        return mySell.getId();
    }

    @Transactional
    public Long save(CreateSellDto createSellDto, Member loginMember) throws IOException {
        List<ProductImage> productImages = fileStore.storeImages(createSellDto.getImageFiles());
        Place place = new Place(createSellDto.getPlace(), createSellDto.getLatitude(), createSellDto.getLongitude());
        Sell sell = Sell.createSell(loginMember, productImages, createSellDto.getTitle(),
                createSellDto.getContent(), createSellDto.getPrice(),
                createSellDto.getCategory(),place);
        sellRepository.save(sell);
        return sell.getId();
    }

    public SellDetailDto findMySell(Long sellId) {
        List<Sell> mySells = sellRepository.findSellById(sellId);
        List<SellDetailDto> result = mySells.stream().map(mySell -> new SellDetailDto(mySell))
                .collect(Collectors.toList());
        return result.get(0);
    }

    public EditSellDto findMySimpleSell(Long sellId) {
        List<Sell> mySells = sellRepository.findMySimpleSellById(sellId);
        List<EditSellDto> result = mySells.stream().map(mySell -> new EditSellDto(mySell))
                .collect(Collectors.toList());
        return result.get(0);
    }

    @Transactional
    public void delete(Long sellId) {
        List<Sell> mySells = sellRepository.findMySimpleSellById(sellId);
        sellRepository.delete(mySells.get(0));
    }

    public List<SellDto> findMySells(Long memberId, SellStatus sellStatus) {
        String filteringQuery = "";
        if(sellStatus != SellStatus.판매완료){
            filteringQuery = " and s.sellStatus not in ('판매완료')";
        }else{
            filteringQuery = " and s.sellStatus in ('판매완료')";
        }
        List<Sell> mySells = sellRepository.findMySellsByMemberId(memberId,filteringQuery);
        return mySells.stream().map(mySell -> new SellDto(mySell))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStatus(Long sellId, SellStatus sellStatus) {
        Sell sell = sellRepository.updateStatus(sellId, sellStatus);
    }

    public List<SellDto> findSells(Long memberId) {
        List<Sell> sells = sellRepository.findSells(memberId);
        return sells.stream().map(mySell -> new SellDto(mySell))
                .collect(Collectors.toList());
    }

    public Map<String, Object> findSell(Long sellId, Long sellerId, Member loginMember) {
        List<Sell> sells = sellRepository.findSellById(sellId);
        List<SellDetailDto> sellDetailDtos = sells.stream().map(sell -> new SellDetailDto(sell))
                .collect(Collectors.toList());
        String filteringQuery = "and s.sellStatus not in ('판매완료')";
        List<Sell> otherSells = sellRepository.findOtherSells(sellId, sellerId,filteringQuery,4);
        List<SellDto> otherSellDtos = otherSells.stream().map(sell -> new SellDto(sell))
                .collect(Collectors.toList());
        SellLike sellLike = sellLikeRepository.findLike(sellId, loginMember.getId());
        Map<String, Object> sellDetailMap = new HashMap<>();
        sellDetailMap.put("sell",sellDetailDtos.get(0));
        sellDetailMap.put("otherSells", otherSellDtos);
        sellDetailMap.put("sellLike",sellLike);

        return sellDetailMap;
    }

    public List<SellDto> findOtherSells(Long sellId, Long memberId,SellStatus sellStatus) {
        String filteringQuery = "";
        if(sellStatus != SellStatus.판매완료 && sellStatus != null){
            filteringQuery = "and s.sellStatus not in ('판매완료')";
        }else if(sellStatus == SellStatus.판매완료){
            filteringQuery = "and s.sellStatus in ('판매완료')";
        }
        List<Sell> sells = sellRepository.findOtherSells(sellId,memberId,filteringQuery,30);
        return sells.stream().map(sell -> new SellDto(sell))
                .collect(Collectors.toList());
    }
}