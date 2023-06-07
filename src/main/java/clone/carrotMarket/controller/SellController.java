package clone.carrotMarket.controller;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.dto.CreateSellDto;
import clone.carrotMarket.file.FileStore;
import clone.carrotMarket.repository.SellRepository;
import clone.carrotMarket.web.argumentresolver.Login;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/sells")
public class SellController {
    private final FileStore fileStore;
    private final SellRepository sellRepository;

    @ModelAttribute("categories")
    public List<Category> caategory() {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.Digital);
        categories.add(Category.Life);
        categories.add(Category.Interior);
        return categories;
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("sell",new CreateSellDto());
        return "sells/addForm";
    }

    @PostMapping("/add")
    public String saveSell(@Valid @ModelAttribute CreateSellDto createSellDto, BindingResult result,
                           @Login Member loginMember, RedirectAttributes redirectAttributes) throws IOException {
        if(result.hasErrors()){
            return "sells/addForm";
        }
        if(loginMember == null){
            return "redirect:/members/login";
        }
        List<ProductImage> productImages = fileStore.storeImages(createSellDto.getImageFiles());
        Place place = new Place(createSellDto.getPlace(), createSellDto.getLatitude(), createSellDto.getLongitude());
        Sell sell = Sell.createSell(loginMember, productImages, createSellDto.getTitle(),
                createSellDto.getContent(), createSellDto.getPrice(),
                createSellDto.getCategory(),place);
        sellRepository.save(sell);
        return "sells/sellDetail";
    }
}
