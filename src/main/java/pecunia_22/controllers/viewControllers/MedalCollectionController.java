package pecunia_22.controllers.viewControllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pecunia_22.models.Medal;
import pecunia_22.models.dto.medal.MedalDto;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.security.config.UserCheckLoged;
import pecunia_22.services.medalService.MedalServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MedalCollectionController {

    private MedalServiceImpl medalService;
    private UserCheckLoged userCheckLoged;

    @Autowired
    public MedalCollectionController(MedalServiceImpl medalService, UserCheckLoged userCheckLoged) {
        this.medalService = medalService;
        this.userCheckLoged = userCheckLoged;
    }

    @GetMapping("/medal/collection")
    public String getIndex(ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();

        List<CountryByStatus> countryByStatusList = new ArrayList<>();
        if (role == "ADMIN") {
            countryByStatusList = medalService.getCountryByStatus("KOLEKCJA", role);
        } else {
            countryByStatusList = medalService.getCountryByStatus("KOLEKCJA", role);
            System.out.println("Brak uprawnień to tu");
            modelMap.addAttribute("error", "Brak Uprawnień");
//            return "error";
        }
        modelMap.addAttribute("countryByStatusList", countryByStatusList);
        return "medal/collection/index";
    }

    @GetMapping("/medal/collection/currency/")
    public String getCurrency(@RequestParam("selectCountryId") Long countryId, ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();

        System.out.println(countryId);
        List<CurrencyByStatus> currencyByStatusList = new ArrayList<>();
        if (role == "ADMIN") {
            currencyByStatusList = medalService.getCurrencyByStatus(countryId, "KOLEKCJA", role);
        } else {
            currencyByStatusList = medalService.getCurrencyByStatus(countryId, "KOLEKCJA", role);
        }
        modelMap.addAttribute("currencyByStatusList", currencyByStatusList);
        System.out.println(JsonUtils.gsonPretty(currencyByStatusList));
        return "medal/collection/currency";
    }

    @GetMapping("/medal/collection/medals/")
    public String getMedal(@RequestParam("selectCurrencyId") Long currencyId, ModelMap modelMap) {

        return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
    }

    @GetMapping("/medal/collection/medals/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("currencyId") Long currencyId,
                                @RequestParam("status") String status, ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
        Integer pageSize =10;

        Page<Medal> page = medalService.findMedalPaginated(pageNo, pageSize, currencyId, status, role);
        List<MedalDto> medalDtoList = new ArrayList<>();

        if (page.getTotalPages() >= pageNo) {
            for (Medal medal : page.getContent()) {
                medalDtoList.add(new ModelMapper().map(medal, MedalDto.class));
            }

            String pathPage = "/medal/collection/medals/page/";
            modelMap.addAttribute("currentPage", pageNo);
            modelMap.addAttribute("totalPages", page.getTotalPages());
            modelMap.addAttribute("totalItems", page.getTotalElements());
            modelMap.addAttribute("pageSize", pageSize);
            modelMap.addAttribute("pathPage", pathPage);

            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
            System.out.println(page.getTotalElements());
            System.out.println(page.getTotalPages());
            System.out.println(page.getSize());

            modelMap.addAttribute("medals", medalDtoList);
            System.out.println(role);
            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
            return "medal/collection/medals";
        }else {
            return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
        }
    }

    @GetMapping("/medal/collection/show/{medalId}")
    public String getShow(@PathVariable Long medalId, ModelMap modelMap) {

        Medal medal = medalService.getMedalById(medalId);
        MedalDto medalDto = new ModelMapper().map(medal, MedalDto.class);
        modelMap.addAttribute("medal", medalDto);
        System.out.println(medalDto.getCurrencies().getId());

        return "medal/collection/show";
    }
}
