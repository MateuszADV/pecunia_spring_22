package pecunia_22.controllers.viewControllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pecunia_22.models.*;
import pecunia_22.models.dto.ImageType.ImageTypeDtoSelect;
import pecunia_22.models.dto.active.ActiveDtoSelect;
import pecunia_22.models.dto.bought.BoughtDto;
import pecunia_22.models.dto.country.CountryDtoForm;
import pecunia_22.models.dto.currency.CurrencyDto;
import pecunia_22.models.dto.currency.CurrencyDtoByPattern;
import pecunia_22.models.dto.making.MakingDtoSelect;
import pecunia_22.models.dto.medal.MedalDto;
import pecunia_22.models.dto.medal.MedalDtoByCurrency;
import pecunia_22.models.dto.medal.MedalDtoForm;
import pecunia_22.models.dto.quality.QualityDtoSelect;
import pecunia_22.models.dto.status.StatusDtoSelect;
import pecunia_22.models.others.variable.VariableForm;
import pecunia_22.services.active.ActiveServiceImpl;
import pecunia_22.services.boughtService.BoughtServiceImpl;
import pecunia_22.services.countryService.CountryServiceImpl;
import pecunia_22.services.currencyService.CurrencyServiceImpl;
import pecunia_22.services.imageTypeService.ImageTypeServiceImpl;
import pecunia_22.services.makingService.MakingServiceImpl;
import pecunia_22.services.medalService.MedalServiceImpl;
import pecunia_22.services.qualityService.QualityServiceImpl;
import pecunia_22.services.status.StatusServiceImpl;
import utils.JsonUtils;
import utils.Search;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return "medal/medal_list";
    }

    @GetMapping("/medal/new/")
    public String getNew(@RequestParam(value = "curId") Long currencyId,
                         ModelMap modelMap) {
        Currency currency = currencyService.getCurrencyById(currencyId);
        CurrencyDto currencyDto = new ModelMapper().map(currency, CurrencyDto.class);

        formVariable(modelMap, currency);

        MedalDtoForm medalDtoForm = new MedalDtoForm();
        medalDtoForm.setCurrencies(currencyDto);

        medalDtoForm.setDateBuy(Date.valueOf(LocalDate.now()));
        medalDtoForm.setPriceBuy(0.00);
        medalDtoForm.setPriceSell(0.00);
        modelMap.addAttribute("medalForm", medalDtoForm);
        return "medal/new";
    }

    @PostMapping("/medal/new")
    public String postNew(@ModelAttribute("medalForm")@Valid MedalDtoForm medalForm, BindingResult result,
                          HttpServletRequest request,
                          ModelMap modelMap) {

        if (result.hasErrors()) {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&ERROR&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            System.out.println(result.toString());
            System.out.println(result.hasFieldErrors("dateBuy"));
            System.out.println(result.resolveMessageCodes("test błedu", "dateBuy").toString());

            if (result.hasFieldErrors("dateBuy")) {
                modelMap.addAttribute("errorDateBuy", "Podaj porawną datę");
            }
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&ERROR END&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            Currency currency = currencyService.getCurrencyById(medalForm.getCurrencies().getId());
            formVariable(modelMap, currency);
            return "medal/new";
        }

        Currency currency = currencyService.getCurrencyById(medalForm.getCurrencies().getId());
        Medal medal = new ModelMapper().map(medalForm, Medal.class);
        medalService.saveMedal(medal);

        return "redirect:/medal/medal_list/?currencySeries=" + currency.getCurrencySeries() + "&curId=" + currency.getId();
    }

    @GetMapping("/medal/edit/{medalId}")
    public String getEdit(@PathVariable Long medalId,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          ModelMap modelMap) {
        Optional<Medal> medal = Optional.ofNullable(medalService.getMedalById(medalId));
        MedalDtoForm medalDtoForm = new ModelMapper().map(medal, MedalDtoForm.class);
        modelMap.addAttribute("medalForm", medalDtoForm);
        modelMap.addAttribute("medalInfoLightBox", medalDtoForm);
        formVariable(modelMap, medal.get().getCurrencies());
        return "medal/edit";
    }

    @PostMapping("/medal/edit")
    public String postEdit(@ModelAttribute ("medalForm")@Valid MedalDtoForm medalForm, BindingResult result,
                           HttpServletRequest request,
                           ModelMap modelMap) {
        if (result.hasErrors()) {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&ERROR&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            Optional<Medal> medal = Optional.ofNullable(medalService.getMedalById(medalForm.getId()));
            MedalDtoForm medalInfoLightBox = new ModelMapper().map(medal, MedalDtoForm.class);
            modelMap.addAttribute("medalInfoLightBox", medalInfoLightBox);

            System.out.println(result.toString());
            System.out.println(result.hasFieldErrors("dateBuy"));
            System.out.println(result.resolveMessageCodes("test błedu", "dateBuy").toString());

            if (result.hasFieldErrors("dateBuy")) {
                System.out.println(result.getFieldError("dateBuy").getDefaultMessage());
                System.out.println(result.getFieldError("dateBuy").getCode());
                modelMap.addAttribute("errorDateBuy", "Podaj porawną datę");
            }
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&ERROR END&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            Currency currency = currencyService.getCurrencyById(medalForm.getCurrencies().getId());

            formVariable(modelMap, currency);
            return "medal/edit";
        }

        Currency currency = currencyService.getCurrencyById(medalForm.getCurrencies().getId());
        System.out.println(JsonUtils.gsonPretty(medalForm));
        System.out.println("++++++++++++++++++++++++++++++STOP+++++++++++++++++++++++++++++++");

        Medal medal = new ModelMapper().map(medalForm, Medal.class);


        System.out.println("+++++++++++++++++ UPDATE START +++++++++++++++++++++++++");
        Long start = System.currentTimeMillis();
        medalService.updateMedal(medal);
        Long stop = System.currentTimeMillis();
        System.out.println(stop - start);
        System.out.println("++++++++++++++++++UPDATE END +++++++++++++++++++++++++++");

        return "redirect:/medal/medal_list/?currencySeries=" + currency.getCurrencySeries() + "&curId=" + currency.getId();
    }

    @GetMapping("/medal/delete/{medalId}")
    public String getDelete(@PathVariable Long medalId, HttpServletRequest request, ModelMap modelMap) {
        Medal medal = medalService.getMedalById(medalId);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^BEGIN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(medal.getCurrencies().getCurrencySeries());
        System.out.println(medal.getCurrencies().getId());
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^END^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        medalService.deleteMedalById(medalId);
        return "redirect:/medal/medal_list/?currencySeries=" + medal.getCurrencies().getCurrencySeries() + "&curId=" + medal.getCurrencies().getId();
    }

    @GetMapping("/medal/show/{medalId}")
    public String getShowMedal(@PathVariable Long medalId, ModelMap modelMap) {
        System.out.println(medalId);
        Medal medal = medalService.getMedalById(medalId);
        MedalDto medalDto = new ModelMapper().map(medal, MedalDto.class);
        System.out.println(JsonUtils.gsonPretty(medalDto));

        modelMap.addAttribute("medal", medalDto);
        modelMap.addAttribute("json", JsonUtils.gsonPretty(medalDto));
        return "medal/show";
    }

    private void formVariable(ModelMap modelMap, Currency currency) {
        List<Currency> currenciesList = currencyService.getCurrencyByCountryByPattern(currency.getCountries().getId(), currency.getPatterns().getPattern());
        List<CurrencyDto> currencyDtos = new ArrayList<>();
        for (Currency currency1 : currenciesList) {
            currencyDtos.add(new ModelMapper().map(currency1, CurrencyDto.class));
        }

        List<Bought> boughts = boughtServices.getAllBought();
        List<BoughtDto> boughtDtos = new ArrayList<>();
        for (Bought bought : boughts) {
            boughtDtos.add(new ModelMapper().map(bought, BoughtDto.class));
        }
        List<Active> actives = activeService.getAllActive();
        List<ActiveDtoSelect> activeDtoSelects = new ArrayList<>();
        for (Active active : actives) {
            activeDtoSelects.add(new ModelMapper().map(active, ActiveDtoSelect.class));
        }

        List<Making> makings = makingService.getAllMakings();
        List<MakingDtoSelect> makingDtoSelects = new ArrayList<>();
        for (Making making : makings) {
            makingDtoSelects.add(new ModelMapper().map(making, MakingDtoSelect.class));
        }

        List<Quality> qualities = qualityService.getAllQuality();
        List<QualityDtoSelect> qualityDtoSelects = new ArrayList<>();
        for (Quality quality : qualities) {
            qualityDtoSelects.add(new ModelMapper().map(quality, QualityDtoSelect.class));
        }

        List<Status> statuses = statusService.getAllStatuses();
        List<StatusDtoSelect> statusDtoSelects = new ArrayList<>();
        for (Status status : statuses) {
            statusDtoSelects.add(new ModelMapper().map(status, StatusDtoSelect.class));
        }

        List<ImageType> imageTypes = imageTypeService.getAllImageTypes();
        List<ImageTypeDtoSelect> imageTypeDtoSelects = new ArrayList<>();
        for (ImageType imageType : imageTypes) {
            imageTypeDtoSelects.add(new ModelMapper().map(imageType, ImageTypeDtoSelect.class));
        }

        VariableForm.variableToSelect(modelMap, currencyDtos, boughtDtos, activeDtoSelects, makingDtoSelects, qualityDtoSelects, statusDtoSelects, imageTypeDtoSelects);
    }
}
