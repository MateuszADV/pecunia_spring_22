package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import pecunia_22.models.others.NBP.GetGoldRateNBP;
import pecunia_22.services.apiService.ApiServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class NBPController {

    private ApiServiceImpl apiService;

    @GetMapping("/nbp")
    public String getIndex(ModelMap modelMap) {

        List<GetGoldRateNBP> getGoldRateNBPList = new ArrayList<>();
        getGoldRateNBPList = apiService.getGoldRateNBP("http://api.nbp.pl/api/cenyzlota/last/10");

        modelMap.addAttribute("npbGoldList", getGoldRateNBPList);
        return "nbp/index";
    }
}
