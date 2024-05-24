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
import pecunia_22.models.Status;
import pecunia_22.models.dto.status.StatusDto;
import pecunia_22.models.dto.status.StatusDtoForm;
import pecunia_22.services.status.StatusServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class StatusController {

    private StatusServiceImpl statusService;

    private Optional<Status> statusTemp;

    @GetMapping("/status")
    public String getIndex(ModelMap modelMap) {
        List<Status> statuses = statusService.getAllStatuses();
        List<StatusDto> statusDtos  = new ArrayList<>();
        for (Status status : statuses) {
            statusDtos.add(new ModelMapper().map(status, StatusDto.class));
        }
        modelMap.addAttribute("statusDtos", statusDtos);
        return "setting/status/index";
    }

    @GetMapping("/status/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("statusForm", new StatusDtoForm());
        return "setting/status/new";
    }

    @PostMapping("status/new")
    public String postNew(@ModelAttribute("statusForm") @Valid StatusDtoForm statusDtoForm,
                          BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            return "setting/status/new";
        }

        System.out.println("***********************Start**********************");
        System.out.println(JsonUtils.gsonPretty(statusDtoForm));
        System.out.println("***********************Stop**********************");

        Status status = new ModelMapper().map(statusDtoForm, Status.class);
        statusService.saveStatus(status);

        return "redirect:/status";
    }

    @GetMapping("/status/edit/{statusId}")
    public String getEdit(@PathVariable Long statusId, ModelMap modelMap) {
        statusTemp = Optional.ofNullable(statusService.getStatusById(statusId));
        StatusDtoForm statusDtoForm = new ModelMapper().map(statusTemp.get(), StatusDtoForm.class);
        modelMap.addAttribute("statusForm", statusDtoForm);
        return "setting/status/edit";
    }

    @PostMapping("status/edit")
    public String postEdit(@ModelAttribute("statusForm") @Valid StatusDtoForm statusDtoForm,
                           BindingResult result, ModelMap modelMap) {
        statusDtoForm.setId(statusTemp.get().getId());
        statusDtoForm.setCreated_at(statusTemp.get().getCreated_at());
        Status status = new ModelMapper().map(statusDtoForm, Status.class);

        statusService.saveStatus(status);
        statusTemp = null;
        return "redirect:/status";
    }

    @GetMapping("status/delete/{statusId}")
    public String getDelete(@PathVariable Long statusId, ModelMap modelMap) {
        try{
            statusService.deleteStatusById(statusId);
            return "redirect:/status";
        } catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
