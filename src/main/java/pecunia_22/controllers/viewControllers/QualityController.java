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
import pecunia_22.models.Quality;
import pecunia_22.models.dto.quality.QualityDto;
import pecunia_22.models.dto.quality.QualityDtoForm;
import pecunia_22.services.qualityService.QualityServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class QualityController {

    private QualityServiceImpl qualityService;

    public Optional<Quality> qualityTemp;

    @GetMapping("/quality")
    public String getIndex(ModelMap modelMap) {
        try {
            List<Quality> qualities = qualityService.getAllQuality();
            List<QualityDto> qualityDtos = new ArrayList<>();
            for (Quality quality : qualities) {
                qualityDtos.add(new ModelMapper().map(quality, QualityDto.class));
            }

            modelMap.addAttribute("qualityDtos", qualityDtos);
            return "setting/quality/index";

        } catch (Exception e){
            System.out.println(e.getMessage());
            modelMap.addAttribute("qualityDtos", null);
            return "setting/quality/index";
        }
    }

    @GetMapping("/quality/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("qualityForm", new QualityDtoForm());
        return "setting/quality/new";
    }

    @PostMapping("/quality/new")
    public String postNew(@ModelAttribute("qualityForm")@Valid QualityDtoForm qualityDtoForm, BindingResult result) {
        System.out.println(JsonUtils.gsonPretty(qualityDtoForm));
        Quality quality = new ModelMapper().map(qualityDtoForm, Quality.class);

        if (result.hasErrors()) {

            return "setting/quality/new";
        }
        qualityService.saveQuality(quality);
        return "redirect:/quality";
    }

    @GetMapping("/quality/edit/{qualityId}")
    public String getEdit(@PathVariable Long qualityId, ModelMap modelMap) {
        try {
            qualityTemp = Optional.ofNullable(qualityService.getQualityById(qualityId));
            QualityDtoForm qualityDtoForm = new ModelMapper().map(qualityTemp.get(), QualityDtoForm.class);
            modelMap.addAttribute("qualityForm", qualityDtoForm);
            return "setting/quality/edit";
        }catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/quality/edit")
    public String postEdit(@ModelAttribute("qualityForm")@Valid QualityDtoForm qualityDtoForm, BindingResult result,
                           ModelMap modelMap) {
        if (result.hasErrors()) {

            return "setting/quality/edit";
        }
        qualityDtoForm.setId(qualityTemp.get().getId());
        qualityDtoForm.setCreated_at(qualityTemp.get().getCreated_at());
        System.out.println(JsonUtils.gsonPretty(qualityDtoForm));
        Quality quality = new ModelMapper().map(qualityDtoForm, Quality.class);
        qualityService.saveQuality(quality);
        return "redirect:/quality";
    }

    @GetMapping("/quality/delete/{qualityId}")
    public String getDelete(@PathVariable Long qualityId, ModelMap modelMap) {
        try {
            qualityService.deleteQualityById(qualityId);
            return "redirect:/quality";
        } catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
