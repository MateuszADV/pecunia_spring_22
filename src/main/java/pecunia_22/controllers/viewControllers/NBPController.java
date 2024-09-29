package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import pecunia_22.models.others.NBP.*;
import pecunia_22.services.apiService.ApiServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
//        System.out.println(JsonUtils.gsonPretty(getRateCurrency));

        String[] codes = new String[getRateCurrency.getExchangeList().get(0).getRates().size()];
        Object[][] dane;
        dane = new Object[getRateCurrency.getExchangeList().size()][getRateCurrency.getExchangeList().get(0).getRates().size() + 1];
        for (int i = 0; getRateCurrency.getExchangeList().size() > i; i ++) {
            dane[i][0] = getRateCurrency.getExchangeList().get(i).getEffectiveDate();
            for (int j = 0; getRateCurrency.getExchangeList().get(i).getRates().size() > j; j++) {
                dane[i][j + 1] = getRateCurrency.getExchangeList().get(i).getRates().get(j).getMid();
                if (i < 1) {
                    codes[j] = getRateCurrency.getExchangeList().get(i).getRates().get(j).getCod();
                }
            }
        }

        List<Object[]> objects = new ArrayList<>();
        for (Object[] objects1 : dane) {
            objects.add(objects1);
        }
        objects.add(codes);

        System.out.println("---------------CODES-------------------------");
//        System.out.println(JsonUtils.gsonPretty(codes));
        System.out.println(JsonUtils.gsonPretty(objects));
        System.out.println("---------------CODES-------------------------");

        modelMap.addAttribute("exchangeCurrency", exchangeCurrency);
        return "nbp/rate-currency";
    }

}
