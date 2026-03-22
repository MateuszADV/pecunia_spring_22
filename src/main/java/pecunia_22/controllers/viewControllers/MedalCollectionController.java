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
import pecunia_22.exceptions.ResourceNotFoundException;
import pecunia_22.models.Medal;
import pecunia_22.models.dto.medal.MedalDto;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.security.config.UserCheckLoged;
import pecunia_22.services.medalService.MedalServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/medal/collection")
public class MedalCollectionController {

    private final MedalServiceImpl medalService;
    private final UserCheckLoged userCheckLoged;
    private final ModelMapper modelMapper;

    @Autowired
    public MedalCollectionController(MedalServiceImpl medalService, UserCheckLoged userCheckLoged, ModelMapper modelMapper) {
        this.medalService = medalService;
        this.userCheckLoged = userCheckLoged;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String getIndex(ModelMap modelMap) {

        try {
            List<CountryByStatus> countryByStatusList = medalService.getCountryByStatus("KOLEKCJA");
            modelMap.addAttribute("countryByStatusList", countryByStatusList);
            return "medal/collection/index";
        } catch (Exception e) {
            log.error("Error fetching countries by status", e);
            modelMap.addAttribute("message", "Error loading collection.");
            return "error";
        }
//        List<CountryByStatus> countryByStatusList = new ArrayList<>();
//        try {
//            countryByStatusList = medalService.getCountryByStatus("KOLEKCJA");
//            modelMap.addAttribute("countryByStatusList", countryByStatusList);
//            return "medal/collection/index";
//        } catch (Exception e) {
//            log.info("""
//                    Error -> {}
//                    """,
//                    e.getMessage());
//            return "error";
//        }
    }

    @GetMapping("/currency/")
    public String getCurrency(@RequestParam("selectCountryId") Long countryId, ModelMap modelMap) {

        List<CurrencyByStatus> currencyByStatusList = medalService.getCurrencyByStatus(countryId, "KOLEKCJA");
        if (currencyByStatusList.isEmpty()) {
            modelMap.addAttribute("message", "No currencies available for country Id: " + countryId);
        } else {
            modelMap.addAttribute("currencyByStatusList", currencyByStatusList);
        }
        return "medal/collection/currency";

//        List<CurrencyByStatus> currencyByStatusList =
//                medalService.getCurrencyByStatus(countryId, "KOLEKCJA");
//
//        if (currencyByStatusList.isEmpty()) {
//
//            modelMap.addAttribute("message",
//                    "No currencies available for this country Id -> " + countryId);
//
//            return "medal/collection/currency";
//        }
//
//        modelMap.addAttribute("currencyByStatusList", currencyByStatusList);
//
//        return "medal/collection/currency";
    }

    @GetMapping("/medals/")
    public String getMedal(@RequestParam("selectCurrencyId") Long currencyId, ModelMap modelMap) {

        return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
    }

    @GetMapping("/medals/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("currencyId") Long currencyId,
                                @RequestParam("status") String status, ModelMap modelMap) {

        Page<Medal> page = medalService.findMedalPaginated(pageNo, 10, currencyId, status);
        List<MedalDto> medalDtoList = page.getContent()
                .stream()
                .map(m -> modelMapper.map(m, MedalDto.class))
                .toList();

        log.info("""
                        
                        MedalDtoList size-> {}
                        Currency Id -> {}
                        Status -> {}
                        """,
                medalDtoList.size(),
                currencyId,
                status);

        if (medalDtoList.isEmpty() && pageNo > 1) {
            // jeśli strona jest pusta, wróć do strony 1
            return findPaginated(1, currencyId, status, modelMap);
        }

        modelMap.addAttribute("medals", medalDtoList);
        modelMap.addAttribute("currentPage", pageNo);
        modelMap.addAttribute("totalPages", page.getTotalPages());
        modelMap.addAttribute("totalItems", page.getTotalElements());
        modelMap.addAttribute("pageSize", page.getSize());
        modelMap.addAttribute("pathPage", "/medal/collection/medals/page/");

        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
        modelMap.addAttribute("userRole", role);

        return "medal/collection/medals";

//        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
//        Integer pageSize =10;
//
//        Page<Medal> page = medalService.findMedalPaginated(pageNo, pageSize, currencyId, status);
//        List<MedalDto> medalDtoList = new ArrayList<>();
//
//        if (page.getTotalPages() >= pageNo) {
//            for (Medal medal : page.getContent()) {
//                medalDtoList.add(new ModelMapper().map(medal, MedalDto.class));
//            }
//
//            String pathPage = "/medal/collection/medals/page/";
//            modelMap.addAttribute("currentPage", pageNo);
//            modelMap.addAttribute("totalPages", page.getTotalPages());
//            modelMap.addAttribute("totalItems", page.getTotalElements());
//            modelMap.addAttribute("pageSize", pageSize);
//            modelMap.addAttribute("pathPage", pathPage);
//
//            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
//            System.out.println(page.getTotalElements());
//            System.out.println(page.getTotalPages());
//            System.out.println(page.getSize());
//
//            modelMap.addAttribute("medals", medalDtoList);
//            System.out.println(role);
//            System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
//            return "medal/collection/medals";
//        }else {
//            return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
//        }
    }

    @GetMapping("/show/{medalId}")
    public String getShow(@PathVariable Long medalId, ModelMap modelMap) {
        log.info("Medal id: {}", medalId);

        try {
            MedalDto medal = medalService.getMedalDtoById(medalId);
            modelMap.addAttribute("medal", medal);
        } catch (ResourceNotFoundException e) {
            log.warn("""
            
            Medal not found: {}
            """
                    ,medalId);
            modelMap.addAttribute("message", "Medal with id " + medalId + " does not exist");
        }
        return "medal/collection/show";

//        try {
//
//            MedalDto medal = medalService.getMedalDtoById(medalId);
//            modelMap.addAttribute("medal", medal);
//
//        } catch (ResourceNotFoundException e) {
//            modelMap.addAttribute("message",
//                    "Medal with id " + medalId + " does not exist");
//        }
//
//        return "medal/collection/show";
    }
}
