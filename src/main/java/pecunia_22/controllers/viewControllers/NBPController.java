package pecunia_22.controllers.viewControllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import pecunia_22.models.others.NBP.ExchangeCurrency;
import pecunia_22.models.others.NBP.GetGoldRateNBP;
import pecunia_22.models.others.NBP.PriceStatistics;
import pecunia_22.models.others.NBP.RateCurrency;
import pecunia_22.services.apiService.ApiServiceImpl;
import utils.JsonUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
public class NBPController {

    private ApiServiceImpl apiService;

    @GetMapping("/nbp")
    public String getIndex(ModelMap modelMap) {

        List<GetGoldRateNBP> getGoldRateNBPList = new ArrayList<>();
        getGoldRateNBPList = apiService.getGoldRateNBP("http://api.nbp.pl/api/cenyzlota/last/11");

        getGoldRateNBPList.removeFirst();
        modelMap.addAttribute("npbGoldList", getGoldRateNBPList);

//        ************* STATYSTYKA **********************

        List<PriceStatistics> priceStatisticsList = new ArrayList<>();
        priceStatisticsList = apiService.PriceStatistics("http://api.nbp.pl/api/cenyzlota/last/", 255);

        modelMap.addAttribute("priceStatistics", priceStatisticsList);
        return "nbp/index";
    }

    @GetMapping("nbp-rate-currency")
    public String getRateCurrency(ModelMap modelMap) {
        ExchangeCurrency exchangeCurrency = apiService.exchangeCurrency("c", "EUR");

        System.out.println(JsonUtils.gsonPretty(exchangeCurrency));
        modelMap.addAttribute("exchangeCurrency", exchangeCurrency);
        return "nbp/rate-currency";
    }

}
