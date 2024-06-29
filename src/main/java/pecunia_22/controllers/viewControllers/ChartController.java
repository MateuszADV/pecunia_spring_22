package pecunia_22.controllers.viewControllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pecunia_22.chartClass.ReportMethod;
import pecunia_22.models.Country;
import pecunia_22.models.dto.country.CountryDto;
import pecunia_22.models.repositories.ChartRepository;
import pecunia_22.models.repositories.CountryRepository;
import pecunia_22.services.chartServices.ChartServiceImpl;
import pecunia_22.services.countryService.CountryServiceImpl;
import utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChartController {
    private CountryRepository countryRepository;
    private ChartServiceImpl chartService;
    public ChartRepository chartRepository;
    private CountryServiceImpl countryService;

    @Autowired
    public ChartController(CountryRepository countryRepository, ChartServiceImpl chartService, ChartRepository chartRepository, CountryServiceImpl countryService) {
        this.countryRepository = countryRepository;
        this.chartService = chartService;
        this.chartRepository = chartRepository;
        this.countryService = countryService;
    }

    @GetMapping("/chart")
    public String getIndex(ModelMap modelMap) throws IOException {
//        modelMap.addAttribute("typeChart","bar");
//        modelMap.addAttribute("reportName", "my_report_continents_test");
////        modelMap.addAttribute("reportName", "my_report_note_currency_country");
////        modelMap.addAttribute("reportName", "my_report_object_status");
////        modelMap.addAttribute("parametr", "England");
//
//        try {
//            List<ReportMethod> reportMethods = chartService.reportMethodList(ChartRepository.class);
//            System.out.println(JsonUtils.gsonPretty(reportMethods));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

//        return "chart/index";
        return postIndex("bar", "my_report_continents_test", "Poland", modelMap);
    }

    @PostMapping("/chart")
    public String postIndex(@RequestParam("typeChart") String typeChart,
                            @RequestParam("report") String report,
                            @RequestParam("countryParam") String countryParam,
                            ModelMap modelMap) {



        List<Country> countries = countryService.getAllCountries();
        List<CountryDto> countryDtos = new ArrayList<>();
        for (Country country : countries) {
            countryDtos.add(new ModelMapper().map(country, CountryDto.class));
        }
        System.out.println("=================================================");
        System.out.println(typeChart);
        System.out.println(countries.size());
        System.out.println("=================================================");
        try {
            List<ReportMethod> reportMethods = chartService.reportMethodList(ChartRepository.class);
            modelMap.addAttribute("reports",reportMethods);
            System.out.println(JsonUtils.gsonPretty(reportMethods));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        modelMap.addAttribute("countries", countryDtos);

        modelMap.addAttribute("typeChart", typeChart);
        modelMap.addAttribute("reportName", report);
        modelMap.addAttribute("countryParam", countryParam);

//        modelMap.addAttribute("reportName", "my_report_note_currency_country");
//        modelMap.addAttribute("reportName", "my_report_object_status");
//        modelMap.addAttribute("parametr", "England");

        return "chart/index";
    }
}
