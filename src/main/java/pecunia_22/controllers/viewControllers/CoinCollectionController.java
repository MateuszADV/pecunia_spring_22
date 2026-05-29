package pecunia_22.controllers.viewControllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pecunia_22.models.Coin;
import pecunia_22.models.dto.coin.CoinDto;
import pecunia_22.models.dto.note.NoteDto;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.security.config.UserCheckLoged;
import pecunia_22.services.coinService.CoinServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequestMapping("/coin/collection")
@Controller
public class CoinCollectionController {

    private final CoinServiceImpl coinService;
    private final UserCheckLoged userCheckLoged;
    private final ModelMapper modelMapper;

    @Autowired
    public CoinCollectionController(CoinServiceImpl coinService, UserCheckLoged userCheckLoged, ModelMapper modelMapper) {
        this.coinService = coinService;
        this.userCheckLoged = userCheckLoged;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
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

    @GetMapping("/currency/")
    public String getCurrency(@RequestParam("selectCountryId") Long countryId, ModelMap modelMap) {

        log.info("""
                
                ---------- Coin Controller -----------
                Country Id -> {}
                """,
                countryId);

        List<CurrencyByStatus> currencyByStatusList = coinService.getCurrencyByStatus(countryId, "KOLEKCJA");

        if (currencyByStatusList.isEmpty()) {
            modelMap.addAttribute("message", "No currencies available for country Id: " + countryId);
        } else {
            modelMap.addAttribute("currencyByStatusList", currencyByStatusList);

        }
//        System.out.println(JsonUtils.gsonPretty(currencyByStatusList));
        return "coin/collection/currency";
    }

    @GetMapping("/coins/")
    public String getCoin(@RequestParam("selectCurrencyId") Long currencyId, ModelMap modelMap) {

        return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
    }

    @GetMapping("/coins/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("currencyId") Long currencyId,
                                @RequestParam("status") String status, ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
        Integer pageSize =10;

        Page<Coin> page = coinService.findCoinPaginated(pageNo, pageSize, currencyId, status, role);

        if (page.isEmpty()) {
            log.warn(
                    """
                            No coins found for currencyId={} 
                            status={}
                            role={}
                            """,
                    currencyId,
                    status,
                    role
            );

            modelMap.addAttribute("coins", Collections.emptyList());

            return "coin/collection/coins";
        }

        if (page.getTotalPages() >= pageNo) {

            List<CoinDto> coinDtoList = page.getContent()
                    .stream()
                    .map(m -> modelMapper.map(m, CoinDto.class))
                    .toList();

            String pathPage = "/coin/collection/coins/page/";
            modelMap.addAttribute("currentPage", pageNo);
            modelMap.addAttribute("totalPages", page.getTotalPages());
            modelMap.addAttribute("totalItems", page.getTotalElements());
            modelMap.addAttribute("pageSize", pageSize);
            modelMap.addAttribute("pathPage", pathPage);

            modelMap.addAttribute("coins", coinDtoList);
            return "coin/collection/coins";
        }else {
            return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
        }

    }

    @GetMapping("/show/{coinId}")
    public String getShow(@PathVariable Long coinId, ModelMap modelMap) {

        Coin coin = coinService.getCoinById(coinId);
        CoinDto coinDto = new ModelMapper().map(coin, CoinDto.class);
        modelMap.addAttribute("coin", coinDto);
        System.out.println(coinDto.getCurrencies().getId());

        return "coin/collection/show";
    }
}