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
import pecunia_22.models.Active;
import pecunia_22.models.dto.active.ActiveDto;
import pecunia_22.models.dto.active.ActiveDtoForm;
import pecunia_22.models.repositories.ActiveRepository;
import pecunia_22.services.active.ActiveServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ActiveController {

    private ActiveServiceImpl activeService;
    private ActiveRepository activeRepository;
    private Optional<Active> activeTemp;

    @GetMapping("/active")
    public String getIndex(ModelMap modelMap) {
        List<ActiveDto> activeDtos = new ArrayList<>();
        List<Active> actives = activeService.getAllActive();
        for (Active active : actives) {
            activeDtos.add(new ModelMapper().map(active, ActiveDto.class));
        }

        modelMap.addAttribute("activeDtos", activeDtos);
        return "active/index";
    }

    @GetMapping("/active/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("activeForm", new ActiveDtoForm());
        return "active/new";
    }

    @PostMapping("/active/new")
    public String getNew(@ModelAttribute("activeForm")@Valid ActiveDtoForm activeDtoForm, BindingResult result, ModelMap modelMap) {
        System.out.println(JsonUtils.gsonPretty(activeDtoForm));
        Active active = new ModelMapper().map(activeDtoForm, Active.class);
        Boolean test = activeService.activeCodExist(activeDtoForm.getActiveCod());
        if (result.hasErrors()) {
            if (test) {
                modelMap.addAttribute("checkActive", test);
            }
            return "active/new";
        }
        if (test) {
            modelMap.addAttribute("checkActive", test);
            return "active/new";
        }
        activeService.saveActive(active);
        return "redirect:/active";
    }

    @GetMapping("/active/edit/{activeId}")
    public String getEdit(@PathVariable Long activeId, ModelMap modelMap) {
        activeTemp = activeRepository.findById(activeId);
        Active active = activeService.getActiveById(activeId);
        ActiveDtoForm activeDtoForm = new ModelMapper().map(active, ActiveDtoForm.class);
        modelMap.addAttribute("activeForm", activeDtoForm);
        return "active/edit";
    }

    @PostMapping("/active/edit")
    public String postEdit(@ModelAttribute("activeForm")@Valid ActiveDtoForm activeDtoForm, BindingResult result, ModelMap modelMap) {
        activeDtoForm.setId(activeTemp.get().getId());
        activeDtoForm.setCreated_at(activeTemp.get().getCreated_at());
        if (result.hasErrors()) {
            if (activeService.getActiveByActiveCod(activeDtoForm.getActiveCod()) != null) {
                if (activeDtoForm.getId() != (activeService.getActiveByActiveCod(activeDtoForm.getActiveCod()).getId())) {
                    modelMap.addAttribute("checkActive", true);
                }
            }
            return "active/edit";
        }

        if (activeService.getActiveByActiveCod(activeDtoForm.getActiveCod()) != null) {
            if (activeDtoForm.getId() != (activeService.getActiveByActiveCod(activeDtoForm.getActiveCod()).getId())) {
                modelMap.addAttribute("checkActive", true);
                return "active/edit";
            }
        }
        Active active = new ModelMapper().map(activeDtoForm, Active.class);

        activeService.saveActive(active);
        return "redirect:/active";
    }

    @GetMapping("/active/delete/{activeId}")
    public String deleteActive(@PathVariable Long activeId, ModelMap modelMap) {

        try {
            activeService.deleteActiveById(activeId);
        } catch (Exception e) {
            System.out.println("*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&");
            System.out.println(e.getMessage());
            System.out.println("*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*&");
            modelMap.addAttribute("error", "Active Not Found For Id :: " + activeId);
            return "error";
        }
        return "redirect:/active";
    }
}
