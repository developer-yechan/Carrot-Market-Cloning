package clone.carrotMarket.controller;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.dto.CreateSellDto;
import clone.carrotMarket.dto.EditSellDto;
import clone.carrotMarket.dto.MySellDetailDto;
import clone.carrotMarket.file.FileStore;
import clone.carrotMarket.service.SellService;
import clone.carrotMarket.web.argumentresolver.Login;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/sells")
public class SellController {
    private final FileStore fileStore;

    private final SellService sellService;


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
        Long sellId = sellService.save(createSellDto, loginMember);
        return "redirect:/sells/my/"+sellId;
    }

    @GetMapping("/my/{sellId}")
    public String mySellDetail(@PathVariable Long sellId, Model model){
        MySellDetailDto mySell = sellService.findMySell(sellId);
        model.addAttribute("mySell",mySell);
        return "sells/mySellDetail";
    }

    @GetMapping("/edit/{sellId}")
    public String editForm(@PathVariable Long sellId, Model model){
        EditSellDto mySimpleSell = sellService.findMySimpleSell(sellId);
        model.addAttribute("sell",mySimpleSell);
        return "sells/editForm";
    }
    @PostMapping("/edit/{sellId}")
    public String editSell(
                           @Valid @ModelAttribute("sell") EditSellDto editSellDto, BindingResult result,
                           @Login Member loginMember, Model model) throws IOException {
        if(result.hasErrors()){
            return "sells/editForm";
        }
        if(loginMember == null){
            return "redirect:/members/login";
        }
        Long sellId = sellService.update(editSellDto);
        return "redirect:/sells/my/"+sellId;
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
