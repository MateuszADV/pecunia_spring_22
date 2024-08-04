package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import pecunia_22.services.apiService.ApiServiceImpl;

@Controller
@AllArgsConstructor
public class NBPController {

    private ApiServiceImpl apiService;

    @GetMapping("/nbp")
    public String getIndex(ModelMap modelMap) {

        apiService.getGoldRateNBP("http://api.nbp.pl/api/cenyzlota/last/10");

        return "nbp/index";
    }
}
