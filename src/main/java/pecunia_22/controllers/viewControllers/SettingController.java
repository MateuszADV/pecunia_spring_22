package pecunia_22.controllers.viewControllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pecunia_22.models.Pattern;
import pecunia_22.models.dto.pattern.PatternDto;
import pecunia_22.models.dto.seting.SettingDto;
import pecunia_22.models.dto.seting.SettingDtoForm;
import pecunia_22.models.setting.Setting;
import pecunia_22.services.setingService.SettingServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class SettingController {

    private SettingServiceImpl settingService;

    @GetMapping("/setting")
    public String getIndex(ModelMap modelMap) {
        List<Setting> settings = settingService.getAllSetting();
        List<SettingDto> settingDtos = new ArrayList<>();
        for (Setting setting : settings) {
            settingDtos.add(new ModelMapper().map(setting, SettingDto.class));
        }
        modelMap.addAttribute("settingDtos", settingDtos);
        return "setting/setting/index";
    }

    @GetMapping("/setting/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("settingForm", new SettingDtoForm());
        return "setting/setting/new";
    }

    @PostMapping("/setting/new")
    public String postNew(@ModelAttribute("settingForm")@Valid SettingDtoForm settingDto, BindingResult result, ModelMap modelMap) {
        if(result.hasErrors()) {
            return "setting/setting/new";
        }
        Setting setting = new ModelMapper().map(settingDto, Setting.class);
        System.out.println("--------------------------------------");
        System.out.println(JsonUtils.gsonPretty(settingDto));
        System.out.println("---------------------------------------");
        settingService.saveSetting(setting);
        System.out.println("))))))))))))))))))))))))))((((((((((((((((((((((((((((");

        return "redirect:/setting";
//        return getShow(pattern.getId(), modelMap);
    }
}
