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
import pecunia_22.models.Pattern;
import pecunia_22.models.dto.pattern.PatternDto;
import pecunia_22.models.repositories.PatternRepository;
import pecunia_22.services.pattern.PatternServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class PatternController {
    private PatternServiceImpl patternService;
    private PatternRepository patternRepository;
    private Optional<Pattern> patternTemp;

    @GetMapping("/pattern")
    public String getIndex(ModelMap modelMap) {
        List<Pattern> patterns = patternService.getAllPattern();
        List<PatternDto> patternDtos = new ArrayList<>();
        for (Pattern pattern : patterns) {
            patternDtos.add(new ModelMapper().map(pattern, PatternDto.class));
        }
        modelMap.addAttribute("patternDtos", patternDtos);
        return "setting/pattern/index";
    }

    @GetMapping("/pattern/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("patternForm", new PatternDto());
        return "setting/pattern/new";
    }

    @PostMapping("/pattern/new")
    public String postNew(@ModelAttribute("patternForm")@Valid PatternDto patternDto, BindingResult result, ModelMap modelMap) {
        if(result.hasErrors()) {
            return "setting/pattern/new";
        }
        Pattern pattern = new ModelMapper().map(patternDto, Pattern.class);
        System.out.println("--------------------------------------");
        System.out.println(JsonUtils.gsonPretty(patternDto));
        System.out.println("---------------------------------------");
        Pattern patternGet = patternService.savePatternGet(pattern);
        System.out.println(JsonUtils.gsonPretty(patternGet));
        System.out.println("))))))))))))))))))))))))))((((((((((((((((((((((((((((");

//        return "redirect:/pattern";
        return getShow(pattern.getId(), modelMap);
    }

    @GetMapping("/pattern/edit/{patternId}")
    public String getEdit(@PathVariable Long patternId, ModelMap modelMap) {
        patternTemp = patternRepository.findById(patternId);
        PatternDto patternDto = new ModelMapper().map(patternTemp, PatternDto.class);
        modelMap.addAttribute("patternForm", patternDto);
        return "setting/pattern/edit";
    }

    @PostMapping("/pattern/edit")
    public String postEdit(@ModelAttribute("patternForm")@Valid PatternDto patternDto, BindingResult result, ModelMap modelMap ){
        if(result.hasErrors()) {
            return "setting/pattern/edit";
        }
        patternDto.setId(patternTemp.get().getId());
        patternDto.setCreated_at(patternTemp.get().getCreated_at());
        Pattern pattern = new ModelMapper().map(patternDto, Pattern.class);
        System.out.println("--------------------------------------");
        System.out.println(JsonUtils.gsonPretty(patternDto));
        System.out.println("---------------------------------------");
        Pattern patternGet = patternService.savePatternGet(pattern);
        return getShow(pattern.getId(), modelMap);
    }


    @GetMapping("/pattern/show/{patternId}")
    public String getShow(@PathVariable Long patternId, ModelMap modelMap) {
        Pattern pattern = patternService.getPatternById(patternId);
        PatternDto patternDto = new ModelMapper().map(pattern, PatternDto.class);
        modelMap.addAttribute("pattern", patternDto);
        return "setting/pattern/show";
    }

    @GetMapping("/pattern/delete/{patternId}")
    public String deletePattern(@PathVariable Long patternId, ModelMap modelMap) {
        patternService.deletePatternById(patternId);
        return "redirect:/pattern";
    }
}
