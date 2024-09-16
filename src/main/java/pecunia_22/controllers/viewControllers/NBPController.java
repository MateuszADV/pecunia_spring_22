package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import pecunia_22.models.others.NBP.*;
import pecunia_22.services.apiService.ApiServiceImpl;
import utils.JsonUtils;

import java.util.List;

@Controller
@AllArgsConstructor
public class NBPController {

    private ApiServiceImpl apiService;

    @GetMapping("/nbp")
    public String getIndex(ModelMap modelMap) {

        List<GetGoldRateNBP> getGoldRateNBPList;
        getGoldRateNBPList = apiService.getGoldRateNBP("https://api.nbp.pl/api/cenyzlota/last/11");

        getGoldRateNBPList.removeFirst();
        modelMap.addAttribute("npbGoldList", getGoldRateNBPList);

//        ************* STATYSTYKA **********************

        List<PriceStatistics> priceStatisticsList;
        priceStatisticsList = apiService.PriceStatistics("https://api.nbp.pl/api/cenyzlota/last/", 255);

        modelMap.addAttribute("priceStatistics", priceStatisticsList);
        return "nbp/index";
    }

    @GetMapping("nbp-rate-currency")
    public String getRateCurrency(ModelMap modelMap) {
        ExchangeCurrency exchangeCurrency = apiService.exchangeCurrency("c", "EUR");

        GetRateCurrency getRateCurrency = apiService.getRateCurrency("A");
        System.out.println(JsonUtils.gsonPretty(getRateCurrency));

//        System.out.println(JsonUtils.gsonPretty(exchangeCurrency));
        modelMap.addAttribute("exchangeCurrency", exchangeCurrency);
        return "nbp/rate-currency";
    }

}
