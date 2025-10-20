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
import pecunia_22.models.dto.coin.CoinDto;
import pecunia_22.models.dto.coin.CoinDtoByCurrency;
import pecunia_22.models.dto.coin.CoinDtoForm;
import pecunia_22.models.dto.country.CountryDtoForm;
import pecunia_22.models.dto.currency.CurrencyDto;
import pecunia_22.models.dto.currency.CurrencyDtoByPattern;
import pecunia_22.models.dto.making.MakingDtoSelect;
import pecunia_22.models.dto.quality.QualityDtoSelect;
import pecunia_22.models.dto.status.StatusDtoSelect;
import pecunia_22.models.others.variable.VariableForm;
import pecunia_22.services.active.ActiveServiceImpl;
import pecunia_22.services.boughtService.BoughtServiceImpl;
import pecunia_22.services.coinService.CoinServiceImpl;
import pecunia_22.services.countryService.CountryServiceImpl;
import pecunia_22.services.currencyService.CurrencyServiceImpl;
import pecunia_22.services.imageTypeService.ImageTypeServiceImpl;
import pecunia_22.services.makingService.MakingServiceImpl;
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
public class CoinController {

    private CountryServiceImpl countryService;
    private CurrencyServiceImpl currencyService;
    private CoinServiceImpl coinService;
    private BoughtServiceImpl boughtServices;
    private ActiveServiceImpl activeService;
    private MakingServiceImpl makingService;
    private QualityServiceImpl qualityService;
    private StatusServiceImpl statusService;
    private ImageTypeServiceImpl imageTypeService;

    Optional<Coin> coinTmp;

    @GetMapping("/coin")
    public String getIndex(ModelMap modelMap) {
        return getSearch("", modelMap);
    }

    @GetMapping("/coin/currency/{countryEn}")
    public String getCoinCurrency(@PathVariable String countryEn, ModelMap modelMap) {
        Country country = countryService.getCountyByCountryEn(countryEn);
        CountryDtoForm countryDto = new ModelMapper().map(country, CountryDtoForm.class);
        List<Currency> currencies = currencyService.getCurrencyByCountryByPattern(countryDto.getId(), "COIN");
        List<CurrencyDtoByPattern> currencyDtoByPatterns = new ArrayList<>();
        for (Currency currency : currencies) {
            currencyDtoByPatterns.add(new ModelMapper().map(currency, CurrencyDtoByPattern.class));
        }
        modelMap.addAttribute("currencies", currencyDtoByPatterns);
        return "coin/currency";
    }

    @PostMapping("/coin/search")
    public String getSearch(@RequestParam(value = "keyword") String keyword, ModelMap modelMap) {
        Search.searchCountry(keyword, modelMap, countryService);
        return "coin/index";
    }

    @GetMapping("/coin/coin_list/")
    public String getCoinList(@RequestParam(value = "currencySeries") String currencySeries,
                              @RequestParam(value = "curId") Long  currencyId,
                              HttpServletRequest request,
                              ModelMap modelMap) {

        Currency currency = currencyService.getCurrencyById(currencyId);
        CurrencyDtoByPattern currencyDtoByPattern = new ModelMapper().map(currency, CurrencyDtoByPattern.class);
        List<Coin> coins = coinService.getCoinByCurrencyId(currencyId);
        List<CoinDtoByCurrency> coinDtoByCurrencies = new ArrayList();
        for (Coin coin : coins) {
            coinDtoByCurrencies.add(new ModelMapper().map(coin, CoinDtoByCurrency.class));
        }

        modelMap.addAttribute("currency", currencyDtoByPattern);
        modelMap.addAttribute("coins", coinDtoByCurrencies);
        return "coin/coin_list";
    }

    @GetMapping("/coin/new/")
    public String getNew(@RequestParam(value = "curId") Long currencyId,
                         ModelMap modelMap) {
        Currency currency = currencyService.getCurrencyById(currencyId);
        CurrencyDto currencyDto = new ModelMapper().map(currency, CurrencyDto.class);

        formVariable(modelMap, currency);

        CoinDtoForm coinDtoForm = new CoinDtoForm();
        coinDtoForm.setCurrencies(currencyDto);

        coinDtoForm.setDateBuy(Date.valueOf(LocalDate.now()));
        coinDtoForm.setPriceBuy(0.00);
        coinDtoForm.setPriceSell(0.00);
        modelMap.addAttribute("coinForm", coinDtoForm);
        return "coin/new";
    }

    @PostMapping("/coin/new")
    public String postNew(@ModelAttribute("coinForm")@Valid CoinDtoForm coinForm, BindingResult result,
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
            Currency currency = currencyService.getCurrencyById(coinForm.getCurrencies().getId());
            formVariable(modelMap, currency);
            return "coin/new";
        }

        Currency currency = currencyService.getCurrencyById(coinForm.getCurrencies().getId());
        Coin coin = new ModelMapper().map(coinForm, Coin.class);
        coinService.saveCoin(coin);

//        return getCoinList(currency.getCurrencySeries(), currency.getId(), request, modelMap);
        return "redirect:/coin/coin_list/?currencySeries=" + currency.getCurrencySeries() + "&curId=" + currency.getId();
    }

    @GetMapping("/coin/edit/{coinId}")
    public String getEdit(@PathVariable Long coinId,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          ModelMap modelMap) {
        Optional<Coin> coin = Optional.ofNullable(coinService.getCoinById(coinId));
        CoinDtoForm coinDtoForm = new ModelMapper().map(coin, CoinDtoForm.class);
        modelMap.addAttribute("coinForm", coinDtoForm);
        modelMap.addAttribute("coinInfoLightBox", coinDtoForm);
        formVariable(modelMap, coin.get().getCurrencies());
        return "coin/edit";
    }

    @PostMapping("/coin/edit")
    public String postEdit(@ModelAttribute ("coinForm")@Valid CoinDtoForm coinForm, BindingResult result,
                           HttpServletRequest request,
                           ModelMap modelMap) {
        if (result.hasErrors()) {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&ERROR&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            Optional<Coin> coin = Optional.ofNullable(coinService.getCoinById(coinForm.getId()));
            CoinDtoForm coinInfoLightBox = new ModelMapper().map(coin, CoinDtoForm.class);
            modelMap.addAttribute("coinInfoLightBox", coinInfoLightBox);

            System.out.println(result.toString());
            System.out.println(result.hasFieldErrors("dateBuy"));
            System.out.println(result.resolveMessageCodes("test błedu", "dateBuy").toString());

            if (result.hasFieldErrors("dateBuy")) {
                System.out.println(result.getFieldError("dateBuy").getDefaultMessage());
                System.out.println(result.getFieldError("dateBuy").getCode());
                modelMap.addAttribute("errorDateBuy", "Podaj porawną datę");
            }
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&ERROR END&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            Currency currency = currencyService.getCurrencyById(coinForm.getCurrencies().getId());

            formVariable(modelMap, currency);
            return "coin/edit";
        }

        Currency currency = currencyService.getCurrencyById(coinForm.getCurrencies().getId());
//        coinForm.setId(coinTmp.get().getId());
//        coinForm.setCreated_at(coinTmp.get().getCreated_at());
        System.out.println(JsonUtils.gsonPretty(coinForm));
        System.out.println("++++++++++++++++++++++++++++++STOP+++++++++++++++++++++++++++++++");

        Coin coin = new ModelMapper().map(coinForm, Coin.class);


        System.out.println("+++++++++++++++++ UPDATE START +++++++++++++++++++++++++");
        Long start = System.currentTimeMillis();
//        coinService.saveCoin(coin);
        coinService.updateCoin(coin);
        Long stop = System.currentTimeMillis();
        System.out.println(stop - start);
        System.out.println("++++++++++++++++++UPDATE END +++++++++++++++++++++++++++");

//        return getcoinList(currency.getCurrencySeries(), currency.getId(), request, modelMap);
        return "redirect:/coin/coin_list/?currencySeries=" + currency.getCurrencySeries() + "&curId=" + currency.getId();
    }

    @GetMapping("/coin/delete/{coinId}")
    public String getDelete(@PathVariable Long coinId, HttpServletRequest request, ModelMap modelMap) {
        Coin coin = coinService.getCoinById(coinId);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^BEGIN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(coin.getCurrencies().getCurrencySeries());
        System.out.println(coin.getCurrencies().getId());
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^END^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        coinService.deleteCoinById(coinId);

//        return getNoteList(note.getCurrencies().getCurrencySeries(), note.getCurrencies().getId(), request, modelMap);
        return "redirect:/coin/coin_list/?currencySeries=" + coin.getCurrencies().getCurrencySeries() + "&curId=" + coin.getCurrencies().getId();
    }

    @GetMapping("/coin/show/{coinId}")
    public String getShowCoin(@PathVariable Long coinId, ModelMap modelMap) {
        System.out.println(coinId);
        Coin coin = coinService.getCoinById(coinId);
        CoinDto coinDto = new ModelMapper().map(coin, CoinDto.class);
        System.out.println(JsonUtils.gsonPretty(coinDto));

        modelMap.addAttribute("coin", coinDto);
        modelMap.addAttribute("json", JsonUtils.gsonPretty(coinDto));
        return "coin/show";
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
