package clone.carrotMarket.controller;

import clone.carrotMarket.domain.*;
import clone.carrotMarket.dto.CreateSellDto;
import clone.carrotMarket.dto.EditSellDto;
import clone.carrotMarket.dto.MySellDetailDto;
import clone.carrotMarket.dto.SellDto;
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

import javax.servlet.http.HttpServletRequest;
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
        return "redirect:/sells/"+sellId;
    }
    //나의 판매글 목록 페이지 Controller
    @GetMapping("/my")
    public String findMySells(@Login Member loginMember, @RequestParam(defaultValue = "판매중") SellStatus sellStatus, Model model){
        List<SellDto> mySells = sellService.findMySells(loginMember.getId(),sellStatus);
        model.addAttribute("sells",mySells);
        return "sells/mySells";
    }

    @GetMapping
    public String findSells(@Login Member loginMember, Model model){
        List<SellDto> mySells = sellService.findSells(loginMember.getId());
        model.addAttribute("myPlace",loginMember.getMyPlace().getPlace());
        model.addAttribute("sells",mySells);
        return "sells/sellList";
    }

    //나의 판매글 상세 페이지 Controller
    @GetMapping("/{sellId}")
    public String mySellDetail(@PathVariable Long sellId, Model model){
        MySellDetailDto mySell = sellService.findMySell(sellId);
        model.addAttribute("sell",mySell);
        return "sells/mySellDetail";
    }

    @GetMapping("/edit/{sellId}")
    public String editForm(@PathVariable Long sellId, Model model){
        EditSellDto mySimpleSell = sellService.findMySimpleSell(sellId);
        model.addAttribute("sell",mySimpleSell);
        return "sells/editForm";
    }
    @PutMapping("/edit/{sellId}")
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
        return "redirect:/sells/"+sellId;
    }

    @PatchMapping("{sellId}/updateStatus")
    public String updateStatus(@PathVariable Long sellId, @RequestParam SellStatus sellStatus, HttpServletRequest request){
        sellService.updateStatus(sellId,sellStatus);
        String referer = request.getHeader("referer");
        String[] refererStrings = referer.split("/");
        String lastString = refererStrings[refererStrings.length-1];
        if(lastString.startsWith("my")){
            return "redirect:/sells/my";
        }
        return "redirect:/sells/"+sellId;
    }

    @DeleteMapping("/{sellId}")
    public String deleteSell(@PathVariable Long sellId){
        sellService.delete(sellId);
        return "redirect:/sells/my";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
