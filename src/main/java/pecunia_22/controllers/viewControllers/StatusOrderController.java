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
import pecunia_22.models.StatusOrder;
import pecunia_22.models.dto.statusOrder.StatusOrderDto;
import pecunia_22.models.dto.statusOrder.StatusOrderDtoForm;
import pecunia_22.services.statusOrderService.StatusOrderServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class StatusOrderController {

    private StatusOrderServiceImpl statusOrderService;

    @GetMapping("/statusOrder")
    public String getIndex(ModelMap modelMap) {
        List<StatusOrder> statusOrders = statusOrderService.getAllStatusOrder();
        List<StatusOrderDto> statusOrderDtos = new ArrayList<>();
        for (StatusOrder statusOrder : statusOrders) {
            statusOrderDtos.add(new ModelMapper().map(statusOrder, StatusOrderDto.class));
        }
        modelMap.addAttribute("statusOrderDtos", statusOrderDtos);
        return "setting/statusOrder/index";
    }

    @GetMapping("/statusOrder/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("statusOrderForm", new StatusOrderDtoForm());
        return "setting/statusOrder/new";
    }

    @PostMapping("/statusOrder/new")
    public String postNew(@ModelAttribute("statusOrderForm")@Valid StatusOrderDtoForm statusOrderDtoForm, BindingResult result, ModelMap modelMap) {
        if(result.hasErrors()) {
            return "setting/statusOrder/new";
        }
        StatusOrder statusOrder = new ModelMapper().map(statusOrderDtoForm, StatusOrder.class);
        System.out.println("--------------------------------------");
        System.out.println(JsonUtils.gsonPretty(statusOrderDtoForm));
        System.out.println("---------------------------------------");
        StatusOrder statusOrderGet = statusOrderService.saveStatusOrderGet(statusOrder);
        System.out.println(JsonUtils.gsonPretty(statusOrder));
        System.out.println("))))))))))))))))))))))))))((((((((((((((((((((((((((((");

        return "redirect:/statusOrder";
//        return getShow(pattern.getId(), modelMap);
    }

    @GetMapping("/statusOrder/edit/{statusOrderId}")
    public String getEdit(@PathVariable Long statusOrderId, ModelMap modelMap) {
//        patternTemp = patternRepository.findById(patternId);
        StatusOrder statusOrder = statusOrderService.getStatusOrderFindById(statusOrderId);
        StatusOrderDtoForm statusOrderForm = new ModelMapper().map(statusOrder, StatusOrderDtoForm.class);
        modelMap.addAttribute("statusOrderForm", statusOrderForm);
        return "setting/statusOrder/edit";
    }

    @PostMapping("/statusOrder/edit")
    public String postEdit(@ModelAttribute("statusOrderForm")@Valid StatusOrderDtoForm statusOrderForm, BindingResult result, ModelMap modelMap ){
        if(result.hasErrors()) {
            return "setting/statusOrder/edit";
        }
        StatusOrder statusOrder = new ModelMapper().map(statusOrderForm, StatusOrder.class);
        System.out.println("--------------------------------------");
        System.out.println(JsonUtils.gsonPretty(statusOrder));
        System.out.println("---------------------------------------");
        StatusOrder statusOrderGet = statusOrderService.saveStatusOrderGet(statusOrder);
//        return getShow(pattern.getId(), modelMap);
        return "redirect:/statusOrder";
    }

    @GetMapping("/statusOrder/delete/{statusOrderId}")
    public String delete(@PathVariable Long statusOrderId, ModelMap modelMap) {
        statusOrderService.deleteStatusOrderById(statusOrderId);
        return "redirect:/statusOrder";
    }
}
