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
import pecunia_22.models.Making;
import pecunia_22.models.dto.making.MakingDto;
import pecunia_22.models.dto.making.MakingDtoForm;
import pecunia_22.models.repositories.MakingRepository;
import pecunia_22.services.makingService.MakingServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class MakingController {
    private MakingServiceImpl makingService;
    private MakingRepository makingRepository;
    private Optional<Making> makingTmp;

    @GetMapping("/making")
    public String getIndex(ModelMap modelMap) {
        List<Making> makings = makingService.getAllMakings();
        List<MakingDto> makingDtos = new ArrayList<>();
        for (Making making : makings) {
            makingDtos.add(new ModelMapper().map(making, MakingDto.class));
        }

        modelMap.addAttribute("makingDtos", makingDtos);
        return "setting/making/index";
    }

    @GetMapping("/making/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("makingForm", new MakingDtoForm());

        return "setting/making/new";
    }

    @PostMapping("/making/new")
    public String postNew(@ModelAttribute("makingForm")@Valid MakingDtoForm makingDtoForm, BindingResult result ) {
        Making making = new ModelMapper().map(makingDtoForm, Making.class);
        if (result.hasErrors()) {
            return "setting/making/new";
        }

        makingService.saveMaking(making);
        return "redirect:/making";
    }

    @GetMapping("/making/edit/{makingId}")
    public String getEdit(@PathVariable Long makingId, ModelMap modelMap ) {
        try {
            makingTmp = Optional.ofNullable(makingService.getMakingById(makingId));
            MakingDtoForm makingDtoForm = new ModelMapper().map(makingTmp, MakingDtoForm.class);
            modelMap.addAttribute("makingForm", makingDtoForm);
            return "setting/making/edit";
        } catch (Exception e){
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/making/edit")
    public String postEdit(@ModelAttribute("makingForm")@Valid MakingDtoForm makingDtoForm, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            return "setting/making/edit";
        }
        makingDtoForm.setId(makingTmp.get().getId());
        makingDtoForm.setCreated_at(makingTmp.get().getCreated_at());
        Making making = new ModelMapper().map(makingDtoForm, Making.class);
        makingService.saveMaking(making);

        return "redirect:/making";
    }

    @GetMapping("/making/delete/{makingId}")
    public String getDelete(@PathVariable Long makingId) {
        makingService.deleteMakingById(makingId);
        return "redirect:/making";
    }
}
