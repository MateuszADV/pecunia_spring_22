package pecunia_22.controllers.viewControllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pecunia_22.models.Active;
import pecunia_22.models.Country;
import pecunia_22.models.Currency;
import pecunia_22.models.Pattern;
import pecunia_22.models.dto.active.ActiveDtoSelect;
import pecunia_22.models.dto.country.CountryGetCurrencyDto;
import pecunia_22.models.dto.country.CountryGetDto;
import pecunia_22.models.dto.currency.CurrencyDto;
import pecunia_22.models.dto.currency.CurrencyDtoForm;
import pecunia_22.models.dto.pattern.PatternDtoCurrency;
import pecunia_22.models.repositories.CurrencyRepository;
import pecunia_22.services.active.ActiveServiceImpl;
import pecunia_22.services.countryService.CountryServiceImpl;
import pecunia_22.services.currencyService.CurrencyServiceImpl;
import pecunia_22.services.pattern.PatternServiceImpl;
import utils.JsonUtils;
import utils.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class CurrencyController {
    private CurrencyRepository currencyRepository;
    private CurrencyServiceImpl currencyService;
    private CountryServiceImpl countryService;
    private ActiveServiceImpl activeService;
    private PatternServiceImpl patternService;

    @GetMapping("/currency")
    public String getIndex(ModelMap modelMap) {


//        return "currency/index";
        return getSearch("", modelMap);
    }

    @GetMapping("/currency/list/{countryId}")
    public String getCountryCurrency(@PathVariable(value = "countryId") Long countryId, ModelMap modelMap) {
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%% START %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("Country Id - " + countryId);
        Country country = countryService.getCountryById(countryId);

        List<CurrencyDto> currencyDtos = new ArrayList<>();
        for (Currency currency : country.getCurrencies()) {
            currencyDtos.add(new ModelMapper().map(currency, CurrencyDto.class));
        }
//        System.out.println(JsonUtils.gsonPretty(currencyDtos));
//        System.out.println(country.getCurrencies().size());

        //TODO Do sprawdzenia to mapowanie
        CountryGetCurrencyDto countryGetCurrencyDto = new ModelMapper().map(country, CountryGetCurrencyDto.class);

        countryGetCurrencyDto.setCurrencyDtos(currencyDtos);
//        System.out.println(countryGetCurrencyDto.getCurrencyDtos().size());
//        System.out.println(JsonUtils.gsonPretty(countryGetCurrencyDto));
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%% END %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        modelMap.addAttribute("country", countryGetCurrencyDto);
        return "currency/list";
    }

    @PostMapping("/currency/search")
    public String getSearch(@RequestParam(value = "keyword") String keyword, ModelMap modelMap) {
        Search.searchCountry(keyword, modelMap, countryService);
//        System.out.println(JsonUtils.gsonPretty(countryGetDtos));
        return "currency/index";
    }

    @GetMapping("/currency/new/")
    public String getNew(@RequestParam("countryId") Long countryId, ModelMap modelMap) {
        System.out.println("===================Country ID===================");
        Country country = countryService.getCountryById(countryId);
        CountryGetDto countryGetDto = new ModelMapper().map(country, CountryGetDto.class);
        CurrencyDtoForm currencyDtoForm = new CurrencyDtoForm();
        currencyDtoForm.setCountries(countryGetDto);
        System.out.println(JsonUtils.gsonPretty(currencyDtoForm));

        currencyParameters(currencyDtoForm, modelMap);
        System.out.println("================================================");
        return "currency/new";
    }

    @PostMapping("currency/new")
    public String postNew(@ModelAttribute("currencyForm") @Valid CurrencyDtoForm currencyForm,
                          BindingResult result,
                          ModelMap modelMap) {
        if (result.hasErrors()) {
            System.out.println(result.toString());
            System.out.println(JsonUtils.gsonPretty(currencyForm));
            currencyParameters(currencyForm, modelMap);
            return "currency/new";
        }

        Currency currency = new ModelMapper().map(currencyForm, Currency.class);

        currencyService.saveCurrency(currency);
//        return getCountryCurrency(currencyForm.getCountries().getId(), modelMap);
        return "redirect:/currency/list/" + currencyForm.getCountries().getId();
//        return getSearch("", modelMap);
    }

    private void currencyParameters(@ModelAttribute("currencyForm") @Valid CurrencyDtoForm currencyForm, ModelMap modelMap) {
        List<Active> actives = activeService.getAllActive();
        List<ActiveDtoSelect> activeDtoCurrencies = new ArrayList<>();
        for (Active active : actives) {
            activeDtoCurrencies.add(new ModelMapper().map(active, ActiveDtoSelect.class));
        }

        List<Pattern> patterns = patternService.getAllPattern();
        List<PatternDtoCurrency> patternDtoCurrencies = new ArrayList<>();
        for (Pattern pattern : patterns) {
            patternDtoCurrencies.add(new ModelMapper().map(pattern, PatternDtoCurrency.class));
        }

        modelMap.addAttribute("actives", activeDtoCurrencies);
        modelMap.addAttribute("patterns", patternDtoCurrencies);
        modelMap.addAttribute("currencyForm", currencyForm);
    }

    @GetMapping("currency/edit/{currencyId}")
    public String postEdit(@PathVariable Long currencyId,
                           ModelMap modelMap) {
        Optional<Currency> currencyTemp;
        currencyTemp = currencyRepository.findById(currencyId);
        CurrencyDtoForm currencyDtoForm = new ModelMapper().map(currencyTemp, CurrencyDtoForm.class);
        System.out.println("*****************************START************************************");
        System.out.println(JsonUtils.gsonPretty(currencyDtoForm));
        System.out.println("*****************************STOP************************************");

        List<Active> actives = activeService.getAllActive();
        List<ActiveDtoSelect> activeDtoCurrencies = new ArrayList<>();
        for (Active active : actives) {
            activeDtoCurrencies.add(new ModelMapper().map(active, ActiveDtoSelect.class));
        }

        List<Pattern> patterns = patternService.getAllPattern();
        List<PatternDtoCurrency> patternDtoCurrencies = new ArrayList<>();
        for (Pattern pattern : patterns) {
            patternDtoCurrencies.add(new ModelMapper().map(pattern, PatternDtoCurrency.class));
        }

        modelMap.addAttribute("actives", activeDtoCurrencies);
        modelMap.addAttribute("patterns", patternDtoCurrencies);
        modelMap.addAttribute("currencyForm", currencyDtoForm);
        System.out.println("+++++++++++++++++Strat Currency Edit++++++++++++++");
        System.out.println(currencyId);
//        System.out.println(JsonUtils.gsonPretty(currencyDtoForm));
        return "currency/edit";
    }

    @PostMapping("currency/edit")
    public String postEdit(@ModelAttribute("currencyForm") @Valid CurrencyDtoForm currencyForm,
                           BindingResult result,
                           ModelMap modelMap) {
        System.out.println("--------------------FORM----------------------------");
        System.out.println(JsonUtils.gsonPretty(currencyForm));
        System.out.println("--------------------FORM----------------------------");

        if (result.hasErrors()) {

            List<Active> actives = activeService.getAllActive();
            List<ActiveDtoSelect> activeDtoCurrencies = new ArrayList<>();
            for (Active active : actives) {
                activeDtoCurrencies.add(new ModelMapper().map(active, ActiveDtoSelect.class));
            }

            List<Pattern> patterns = patternService.getAllPattern();
            List<PatternDtoCurrency> patternDtoCurrencies = new ArrayList<>();
            for (Pattern pattern : patterns) {
                patternDtoCurrencies.add(new ModelMapper().map(pattern, PatternDtoCurrency.class));
            }

            modelMap.addAttribute("actives", activeDtoCurrencies);
            modelMap.addAttribute("patterns", patternDtoCurrencies);
            modelMap.addAttribute("currencyForm", currencyForm);

            return "currency/edit";
        }

//        currencyForm.setId(currencyTemp.get().getId());
//        currencyForm.setCreated_at(currencyTemp.get().getCreated_at());

        Currency currency = new ModelMapper().map(currencyForm, Currency.class);

        System.out.println("+++++++++++++++++ UPDATE START +++++++++++++++++++++++++");
        Long start = System.currentTimeMillis();

        currencyService.saveCurrency(currency);

        Long stop = System.currentTimeMillis();
        System.out.println("miliSec - " + (stop - start) + "ms");
        System.out.println("++++++++++++++++++UPDATE END +++++++++++++++++++++++++++");

//        return getCountryCurrency(currencyForm.getCountries().getId(), modelMap);
        return "redirect:/currency/list/" + currencyForm.getCountries().getId();

    }

    @GetMapping("/currency/show/{currencyId}")
    public String getShow(@PathVariable Long currencyId, ModelMap modelMap) {
        Currency currency = currencyService.getCurrencyById(currencyId);
        CurrencyDto currencyDto = new ModelMapper().map(currency, CurrencyDto.class);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(currencyId);
        System.out.println(JsonUtils.gsonPretty(currencyDto));

        modelMap.addAttribute("currency", currencyDto);
        modelMap.addAttribute("json", JsonUtils.gsonPretty(currencyDto));
        return "currency/show";
    }

    @GetMapping("/currency/delete/{currencyId}/{countryId}")
    public String getDelete(@PathVariable Long currencyId, @PathVariable Long countryId, ModelMap modelMap) {
        try {
            currencyService.deleteCurrencyById(currencyId);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }
        return "redirect:/currency/list/" + countryId;
    }


}
