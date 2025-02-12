package pecunia_22.controllers.viewControllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pecunia_22.models.Country;
import pecunia_22.models.Currency;
import pecunia_22.models.Medal;
import pecunia_22.models.dto.country.CountryDtoForm;
import pecunia_22.models.dto.currency.CurrencyDtoByPattern;
import pecunia_22.models.dto.medal.MedalDtoByCurrency;
import pecunia_22.services.active.ActiveServiceImpl;
import pecunia_22.services.boughtService.BoughtServiceImpl;
import pecunia_22.services.countryService.CountryServiceImpl;
import pecunia_22.services.currencyService.CurrencyServiceImpl;
import pecunia_22.services.imageTypeService.ImageTypeServiceImpl;
import pecunia_22.services.makingService.MakingServiceImpl;
import pecunia_22.services.medalService.MedalServiceImpl;
import pecunia_22.services.qualityService.QualityServiceImpl;
import pecunia_22.services.status.StatusServiceImpl;
import utils.Search;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class MedalController {

    private MedalServiceImpl medalService;
    private CountryServiceImpl countryService;
    private CurrencyServiceImpl currencyService;
    private BoughtServiceImpl boughtServices;
    private ActiveServiceImpl activeService;
    private MakingServiceImpl makingService;
    private QualityServiceImpl qualityService;
    private StatusServiceImpl statusService;
    private ImageTypeServiceImpl imageTypeService;

    @GetMapping("/medal")
    public String getIndex(ModelMap modelMap) {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("---------------------MEDAL-----------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------");
        return getSearch("", modelMap);
    }

    @GetMapping("/medal/currency/{countryEn}")
    public String getMedalCurrency(@PathVariable String countryEn, ModelMap modelMap) {
        Country country = countryService.getCountyByCountryEn(countryEn);
        CountryDtoForm countryDto = new ModelMapper().map(country, CountryDtoForm.class);
        List<Currency> currencies = currencyService.getCurrencyByCountryByPattern(countryDto.getId(), "MEDAL");
        List<CurrencyDtoByPattern> currencyDtoByPatterns = new ArrayList<>();
        for (Currency currency : currencies) {
            currencyDtoByPatterns.add(new ModelMapper().map(currency, CurrencyDtoByPattern.class));
        }
        modelMap.addAttribute("currencies", currencyDtoByPatterns);
        return "medal/currency";
    }

    @PostMapping("/medal/search")
    public String getSearch(@RequestParam(value = "keyword") String keyword, ModelMap modelMap) {
        Search.searchCountry(keyword, modelMap, countryService);
        return "medal/index";
    }

    @GetMapping("/medal/medal_list/")
    public String getMedalList(@RequestParam(value = "currencySeries") String currencySeries,
                              @RequestParam(value = "curId") Long  currencyId,
                              HttpServletRequest request,
                              ModelMap modelMap) {

        Currency currency = currencyService.getCurrencyById(currencyId);
        CurrencyDtoByPattern currencyDtoByPattern = new ModelMapper().map(currency, CurrencyDtoByPattern.class);
        List<Medal> medals = medalService.getMedalByCurrencyId(currencyId);
        List<MedalDtoByCurrency> medalDtoByCurrencies = new ArrayList();
        for (Medal medal : medals) {
            medalDtoByCurrencies.add(new ModelMapper().map(medal, MedalDtoByCurrency.class));
        }

        System.out.println("-------------------------Medal size-------------------------------");
        System.out.println(currencyDtoByPattern.getMedals().size());
        System.out.println("-------------------------Medal size-------------------------------");

        modelMap.addAttribute("currency", currencyDtoByPattern);
        modelMap.addAttribute("medals", medalDtoByCurrencies);
        return "/medal/medal_list";
    }
}
