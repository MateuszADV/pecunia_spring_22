package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pecunia_22.models.Continent;
import pecunia_22.models.dto.continent.ContinentCountriesDto;
import pecunia_22.models.dto.continent.ContinentDto;
import pecunia_22.models.repositories.ContinentRepository;
import pecunia_22.services.continentService.ContinentServiceImpl;
import pecunia_22.services.countryService.CountryServiceImpl;
import utils.JsonUtils;

@Controller
@AllArgsConstructor
public class ContinentController {

    private ContinentRepository continentRepository;
    private ContinentServiceImpl continentService;
    private CountryServiceImpl countryService;

    @GetMapping("/continent")
    public String getIndex(ModelMap modelMap) {
        getContinentList(modelMap);

        return "continent/index";
    }

    @GetMapping("/continent/new")
    public String getNew(ModelMap modelMap) {
        modelMap.addAttribute("continentForm", new ContinentDto());
        System.out.println("------------------NEW Continent----------------------");

        return "continent/new";
    }

    @PostMapping("/continent/new")
    public String postNew(@ModelAttribute("countryForm")ContinentDto continentForm, ModelMap modelMap) {
        System.out.println("--------------------***************START*****************-----------------------------");
        System.out.println(JsonUtils.gsonPretty(continentForm));
        Continent continent = new Continent();
        continent = new ModelMapper().map(continentForm, Continent.class);
        continentService.saveContinent(continent);
        System.out.println("--------------------*****************END***************-----------------------------");

        getContinentList(modelMap);

        return "continent/index";
    }

    private void getContinentList(ModelMap modelMap) {
//        List<Continent> continents = continentRepository.findAll();
//        List<ContinentDto> continentDtos = new ArrayList<>();
//        for (Continent continent : continents) {
//            continentDtos.add(new ModelMapper().map(continent, ContinentDto.class));
//        }
//        modelMap.addAttribute("continents", continentDtos);

        //TODO
        modelMap.addAttribute("continents", countryService.countryCounts());
    }

    @GetMapping("/continent/")
    public String getNContinent(@RequestParam("selectContinent") String continentEn,
                                ModelMap modelMap) {
        System.out.println("====================Continent ID============================");
        Continent continent = continentRepository.getContinent(continentEn);
        if (continent == null){
            getContinentList(modelMap);
            return getIndex(modelMap);
        }else {
            ContinentCountriesDto continentCountriesDto = new ModelMapper().map(continent, ContinentCountriesDto.class);
            System.out.println(JsonUtils.gsonPretty(continentCountriesDto));
            modelMap.addAttribute("continent", continentCountriesDto);
        }
        System.out.println("====================Continent ID============================");
        return "continent/country";
    }
}
