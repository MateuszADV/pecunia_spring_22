package pecunia_22.controllers.viewControllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pecunia_22.models.Security;
import pecunia_22.models.dto.security.SecurityDto;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.services.securityService.SecurityServiceImpl;
import utils.JsonUtils;
import utils.Role;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SecurityCollectionController {

    private SecurityServiceImpl securityService;

    @Autowired
    public SecurityCollectionController(SecurityServiceImpl securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/security/collection")
    public String getIndex(ModelMap modelMap) {
        String role = Role.role();

        List<CountryByStatus> countryByStatusList = new ArrayList<>();
        if (role == "ADMIN") {
            countryByStatusList = securityService.getCountryByStatus("KOLEKCJA", role);
        } else {
            countryByStatusList = securityService.getCountryByStatus("KOLEKCJA", role);
            System.out.println("Brak uprawnień to tu");
            modelMap.addAttribute("error", "Brak Uprawnień");
//            return "error";
        }
        modelMap.addAttribute("countryByStatusList", countryByStatusList);
        System.out.println(countryByStatusList.size());
//        System.out.println(JsonUtils.gsonPretty(countryByStatusList));
        return "security/collection/index";
    }

    @GetMapping("/security/collection/currency/")
    public String getCurrency(@RequestParam("selectCountryId") Long countryId, ModelMap modelMap) {
        String role = Role.role();

        System.out.println(countryId);
        List<CurrencyByStatus> currencyByStatusList = new ArrayList<>();
        if (role == "ADMIN") {
            currencyByStatusList = securityService.getCurrencyByStatus(countryId, "KOLEKCJA", role);
        } else {
            currencyByStatusList = securityService.getCurrencyByStatus(countryId, "KOLEKCJA", role);
        }
        modelMap.addAttribute("currencyByStatusList", currencyByStatusList);
        System.out.println(JsonUtils.gsonPretty(currencyByStatusList));
        return "security/collection/currency";
    }

    @GetMapping("/security/collection/securities/")
    public String getSecurity(@RequestParam("selectCurrencyId") Long currencyId, ModelMap modelMap) {

        return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
    }

    @GetMapping("/security/collection/securities/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("currencyId") Long currencyId,
                                @RequestParam("status") String status, ModelMap modelMap) {
        String role = Role.role();
        Integer pageSize =10;

        Page<Security> page = securityService.findSecurityPaginated(pageNo, pageSize, currencyId, status, role);
        List<SecurityDto> securityDtoList = new ArrayList<>();

        if (page.getTotalPages() >= pageNo) {
            for (Security security : page.getContent()) {
                securityDtoList.add(new ModelMapper().map(security, SecurityDto.class));
            }

            String pathPage = "/security/collection/securities/page/";
            modelMap.addAttribute("currentPage", pageNo);
            modelMap.addAttribute("totalPages", page.getTotalPages());
            modelMap.addAttribute("totalItems", page.getTotalElements());
            modelMap.addAttribute("pageSize", pageSize);
            modelMap.addAttribute("pathPage", pathPage);

            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
            System.out.println(page.getTotalElements());
            System.out.println(page.getTotalPages());
            System.out.println(page.getSize());

            modelMap.addAttribute("securities", securityDtoList);
            System.out.println(role);
            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
            return "security/collection/securities";
        }else {
            return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
        }
    }

    @GetMapping("/security/collection/show/{securityId}")
    public String getShow(@PathVariable Long securityId, ModelMap modelMap) {
        Security security = securityService.getSecurityById(securityId);
        SecurityDto securityDto = new ModelMapper().map(security, SecurityDto.class);
        modelMap.addAttribute("security", securityDto);
        System.out.println(securityDto.getCurrencies().getId());

        return "security/collection/show";
    }
}
