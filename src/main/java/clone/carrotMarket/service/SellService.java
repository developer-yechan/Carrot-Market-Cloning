package clone.carrotMarket.service;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.dto.CreateSellDto;
import clone.carrotMarket.dto.EditSellDto;
import clone.carrotMarket.dto.MySellDetailDto;
import clone.carrotMarket.dto.MySellDto;
import clone.carrotMarket.file.FileStore;
import clone.carrotMarket.repository.SellRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class SellService {
    private final SellRepository sellRepository;

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

    public MySellDetailDto findMySell(Long sellId) {
        List<Sell> mySells = sellRepository.findMySellById(sellId);
        System.out.println("mySells = " + mySells);
        List<MySellDetailDto> result = mySells.stream().map(mySell -> new MySellDetailDto(mySell))
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

    public List<MySellDto> findMySells(Long memberId) {
        List<Sell> mySells = sellRepository.findMySellsByMemberId(memberId);
        return mySells.stream().map(mySell -> new MySellDto(mySell))
                .collect(Collectors.toList());

    }

    @Transactional
    public void updateStatus(Long sellId, SellStatus sellStatus) {
        Sell sell = sellRepository.updateStatus(sellId, sellStatus);
        System.out.println("sell = " + sell);
    }
}
