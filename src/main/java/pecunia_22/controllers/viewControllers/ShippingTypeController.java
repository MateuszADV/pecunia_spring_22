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
import pecunia_22.models.ShippingType;
import pecunia_22.models.dto.shippingType.ShippingTypeDto;
import pecunia_22.models.dto.shippingType.ShippingTypeDtoForm;
import pecunia_22.services.shippingType.ShippingTypeServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class ShippingTypeController {

    private ShippingTypeServiceImpl shippingTypeService;

    @GetMapping("/shipping")
    public String getIndex(ModelMap modelMap) {
        List<ShippingType> shippingTypes = shippingTypeService.getAllShippingType();
        List<ShippingTypeDto> shippingTypeDtos = new ArrayList<>();
        for (ShippingType shippingType : shippingTypes) {
            shippingTypeDtos.add(new ModelMapper().map(shippingType, ShippingTypeDto.class));
        }
        modelMap.addAttribute("shippingTypeDtos", shippingTypeDtos);
        return "setting/shippingType/index";
    }

    @GetMapping("/shipping/new")
    public String getNew(ModelMap modelMap) {
        ShippingTypeDtoForm shippingTypeDtoForm = new ShippingTypeDtoForm();
        shippingTypeDtoForm.setShippingCost(0.00);
        modelMap.addAttribute("shippingTypeForm", shippingTypeDtoForm);
        return "setting/shippingType/new";
    }

    @PostMapping("/shipping/new")
    public String postNew(@ModelAttribute("shippingTypeForm")@Valid ShippingTypeDtoForm shippingTypeDtoForm, BindingResult result, ModelMap modelMap) {
        if(result.hasErrors()) {
            return "setting/shippingType/new";
        }
        ShippingType shippingType = new ModelMapper().map(shippingTypeDtoForm, ShippingType.class);
        System.out.println("--------------------------------------");
        System.out.println(JsonUtils.gsonPretty(shippingTypeDtoForm));
        System.out.println("---------------------------------------");
        ShippingType shhippingTypeGet = shippingTypeService.saveShippingTypeGet(shippingType);
        System.out.println(JsonUtils.gsonPretty(shhippingTypeGet));
        System.out.println("))))))))))))))))))))))))))((((((((((((((((((((((((((((");

        return "redirect:/shipping";
//        return getShow(pattern.getId(), modelMap);
    }

    @GetMapping("/shipping/edit/{shippingId}")
    public String getEdit(@PathVariable Long shippingId, ModelMap modelMap) {
//        patternTemp = patternRepository.findById(patternId);
        ShippingType shippingType = shippingTypeService.getShippingTypeFindById(shippingId);
        ShippingTypeDtoForm shippingTypeForm = new ModelMapper().map(shippingType, ShippingTypeDtoForm.class);
        modelMap.addAttribute("shippingTypeForm", shippingTypeForm);
        return "setting/shippingType/edit";
    }

    @PostMapping("/shipping/edit")
    public String postEdit(@ModelAttribute("shippingTypeForm")@Valid ShippingTypeDtoForm shippingTypeForm, BindingResult result, ModelMap modelMap ){
        if(result.hasErrors()) {
            return "setting/shippingType/edit";
        }

        ShippingType shippingType = new ModelMapper().map(shippingTypeForm, ShippingType.class);
        System.out.println("--------------------------------------");
        System.out.println(JsonUtils.gsonPretty(shippingType));
        System.out.println("---------------------------------------");
        ShippingType shippingTypeGet = shippingTypeService.saveShippingTypeGet(shippingType);
//        return getShow(pattern.getId(), modelMap);
        return "redirect:/shipping";
    }

    @GetMapping("/shipping/delete/{shippingId}")
    public String delete(@PathVariable Long shippingId, ModelMap modelMap) {
        shippingTypeService.deleteShippingTypeById(shippingId);
        return "redirect:/shipping";
    }
}
