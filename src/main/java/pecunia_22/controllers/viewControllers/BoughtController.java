package pecunia_22.controllers.viewControllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pecunia_22.models.Bought;
import pecunia_22.models.dto.bought.BoughtDto;
import pecunia_22.models.dto.bought.BoughtDtoForm;
import pecunia_22.services.boughtService.BoughtServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class BoughtController {
    private BoughtServiceImpl boughtServices;

    private Optional<Bought> boughtTemp;

    @GetMapping("/bought")
    public String getIndex(ModelMap modelMap) {
        List<Bought> boughts = boughtServices.getAllOrderById();
        List<BoughtDto> boughtDtos = new ArrayList();
        for (Bought bought : boughts) {
            boughtDtos.add(new ModelMapper().map(bought, BoughtDto.class));
        }

        modelMap.addAttribute("boughtDtos", boughtDtos);
        return "bought/index";
    }

    @GetMapping("/bought/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("boughtForm", new BoughtDtoForm());
        return "bought/new";
    }

    @PostMapping("/bought/new")
    public String posNew(@ModelAttribute("boughtForm")@Valid BoughtDtoForm boughtDtoForm, BindingResult result, ModelMap modelMap) {
        System.out.println(JsonUtils.gsonPretty(boughtDtoForm));

//        Boolean test = activeService.activeCodExist(activeDto.getActiveCod());
        if (result.hasErrors()) {
//            if (test) {
//                modelMap.addAttribute("checkActive", test);
//            }
            return "bought/new";
        }
//        if (test) {
//            modelMap.addAttribute("checkActive", test);
//            return "active/new";
//        }
        Bought bought = new ModelMapper().map(boughtDtoForm, Bought.class);
        boughtServices.saveBought(bought);
        return "redirect:/bought";
    }

    @GetMapping("/bought/edit/{boughtId}")
    public String getEdit(@PathVariable Long boughtId, ModelMap modelMap) {
        boughtTemp = Optional.ofNullable(boughtServices.getBoughtById(boughtId));
        BoughtDtoForm boughtFormDto = new ModelMapper().map(boughtTemp, BoughtDtoForm.class);
        modelMap.addAttribute("boughtForm", boughtFormDto);
        return "bought/edit";
    }

    @PostMapping("/bought/edit")
    public String postEdit(@ModelAttribute("boughtForm")@Valid BoughtDtoForm boughtDtoForm, BindingResult result, ModelMap modelMap) {
        System.out.println(JsonUtils.gsonPretty(boughtDtoForm));

        if (result.hasErrors()) {
            return "bought/edit";
        }

        Bought bought = new ModelMapper().map(boughtDtoForm, Bought.class);
        bought.setId(boughtTemp.get().getId());

        try {
            boughtServices.updateBought(bought);
        }catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/bought";
    }

    @GetMapping("/bought/delete/{id}")
    public String getDelete(@PathVariable Long id, ModelMap modelMap) {
        try {
            boughtServices.deleteBoughtById(id);
            return "redirect:/bought";
        }catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
