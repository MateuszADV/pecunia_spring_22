package pecunia_22.controllers.viewControllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pecunia_22.models.Coin;
import pecunia_22.models.dto.coin.CoinDto;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.security.config.UserCheckLoged;
import pecunia_22.services.coinService.CoinServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CoinCollectionController {

    private CoinServiceImpl coinService;
    private UserCheckLoged userCheckLoged;

    @Autowired
    public CoinCollectionController(CoinServiceImpl coinService, UserCheckLoged userCheckLoged) {
        this.coinService = coinService;
        this.userCheckLoged = userCheckLoged;
    }

    @GetMapping("/coin/collection")
    public String getIndex(ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
        System.out.println("+++++++++++++++++++++++++++++++ROLE+++++++++++++++++++++++++++++++++++");
        System.out.println(role);
        System.out.println("+++++++++++++++++++++++++++++++ROLE+++++++++++++++++++++++++++++++++++");

        List<CountryByStatus> countryByStatusList = new ArrayList<>();
        if (role == "ADMIN") {
            countryByStatusList = coinService.getCountryByStatus("KOLEKCJA", role);
        } else {
            countryByStatusList = coinService.getCountryByStatus("KOLEKCJA", role);
            System.out.println("Brak uprawnień to tu");
//            modelMap.addAttribute("error", "Brak Uprawnień");
//            return "error";
        }
        modelMap.addAttribute("countryByStatusList", countryByStatusList);
        return "coin/collection/index";
    }

    @GetMapping("/coin/collection/currency/")
    public String getCurrency(@RequestParam("selectCountryId") Long countryId, ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();

        System.out.println("==================================================");
        System.out.println("Coin Country");
        System.out.println("==================================================");

        System.out.println(countryId);
        List<CurrencyByStatus> currencyByStatusList = new ArrayList<>();
        if (role == "ADMIN") {
            currencyByStatusList = coinService.getCurrencyByStatus(countryId, "KOLEKCJA", role);
        } else {
            currencyByStatusList = coinService.getCurrencyByStatus(countryId, "KOLEKCJA", role);
        }
        modelMap.addAttribute("currencyByStatusList", currencyByStatusList);
        System.out.println(JsonUtils.gsonPretty(currencyByStatusList));
        return "coin/collection/currency";
    }

    @GetMapping("/coin/collection/coins/")
    public String getCoin(@RequestParam("selectCurrencyId") Long currencyId, ModelMap modelMap) {

        return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
    }

    @GetMapping("/coin/collection/coins/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("currencyId") Long currencyId,
                                @RequestParam("status") String status, ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
        Integer pageSize =10;

        Page<Coin> page = coinService.findCoinPaginated(pageNo, pageSize, currencyId, status, role);
        List<CoinDto> coinDtoList = new ArrayList<>();

        if (page.getTotalPages() >= pageNo) {
            for (Coin coin : page.getContent()) {
                coinDtoList.add(new ModelMapper().map(coin, CoinDto.class));
            }

            String pathPage = "/coin/collection/coins/page/";
            modelMap.addAttribute("currentPage", pageNo);
            modelMap.addAttribute("totalPages", page.getTotalPages());
            modelMap.addAttribute("totalItems", page.getTotalElements());
            modelMap.addAttribute("pageSize", pageSize);
            modelMap.addAttribute("pathPage", pathPage);

            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
            System.out.println(page.getTotalElements());
            System.out.println(page.getTotalPages());
            System.out.println(page.getSize());

            modelMap.addAttribute("coins", coinDtoList);
            System.out.println(role);
            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
            return "coin/collection/coins";
        }else {
            return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
        }

    }

    @GetMapping("/coin/collection/show/{coinId}")
    public String getShow(@PathVariable Long coinId, ModelMap modelMap) {

        Coin coin = coinService.getCoinById(coinId);
        CoinDto coinDto = new ModelMapper().map(coin, CoinDto.class);
        modelMap.addAttribute("coin", coinDto);
        System.out.println(coinDto.getCurrencies().getId());

        return "coin/collection/show";
    }
}