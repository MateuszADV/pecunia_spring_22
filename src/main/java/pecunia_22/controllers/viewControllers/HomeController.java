package pecunia_22.controllers.viewControllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pecunia_22.models.Country;
import pecunia_22.models.dto.UserRegistration;
import pecunia_22.models.dto.country.CountryDtoForm;
import pecunia_22.models.others.GetMetalRate;
import pecunia_22.models.others.GetMetalSymbol;
import pecunia_22.models.others.GetRateCurrencyTableA;
import pecunia_22.models.others.moneyMetals.GetMoneyMetals;
import pecunia_22.models.repositories.CountryRepository;
import pecunia_22.registration.RegistrationRequest;
import pecunia_22.registration.RegistrationService;
import pecunia_22.security.config.UserCheckLoged;
import pecunia_22.services.apiService.ApiServiceImpl;
import utils.JsonUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@AllArgsConstructor
public class HomeController {

    private RegistrationService registrationService;
    private UserCheckLoged userCheckLoged;
    private ApiServiceImpl apiService;
    private CountryRepository countryRepository;

    @GetMapping("/")
    public String getIndex(ModelMap modelMap) {
        GetRateCurrencyTableA getRateCurrencyTableA = new GetRateCurrencyTableA();
        String[] codes = {"EUR", "USD", "GBP", "CHF"};
        try {
            getRateCurrencyTableA = apiService.getRateCurrencyTableA("https://api.nbp.pl/api/exchangerates/tables/A/?format=json", codes);
            modelMap.addAttribute("rateCurrencyTableA", getRateCurrencyTableA);
            System.out.println(JsonUtils.gsonPretty(getRateCurrencyTableA));
            System.out.println("KURS WALUT");
        }catch (Exception e) {
            modelMap.addAttribute("rateCurrencyTableA", getRateCurrencyTableA);
        }
        String url = "https://api.nbp.pl/api/exchangerates/tables/A/?format=json";
        String url2 ="https://api.gold-api.com/symbols";
        String url3 = "https://www.moneymetals.com/api/spot-prices.json";

        Client client = ClientBuilder.newClient();
        Invocation.Builder webResource = client.target(url).request();

        System.out.println("-----------START-------------------");
        System.out.println(webResource.get(String.class));
        String stringJson = webResource.get(String.class);

        System.out.println("-----------------------------------------");
        System.out.println(apiService.webResource(url).get());
        System.out.println(apiService.webResource(url).get(String.class));
        System.out.println(apiService.webResource(url).get().getStatus());
        System.out.println("żłóńćąęś");
        System.out.println("-----------------------------------------");

//        System.out.println(JsonUtils.gsonPretty(webResource.get(Object.class)));

//        System.out.println(webResource.accept("application/json").get().getDate());
//        System.out.println(webResource.accept("application/json").get());
//        System.out.println(webResource.accept("application/json").get().getStatusInfo());
//        System.out.println(webResource.accept("application/json").get().getStatus());
//        System.out.println(webResource.accept("application/json").get().getHeaders());
//        System.out.println(webResource.accept("application/json").buildGet());
//        System.out.println(webResource.accept("application/json").head());
        System.out.println("-----------STOP-------------------");


        return "home/index";
    }

    @GetMapping("/registration")
    public String getRegistration(ModelMap modelMap){
        modelMap.addAttribute("userForm", new UserRegistration());
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println("TU JESTEM REGISTRATION");
        System.out.println("*****************************************************************");
        return "home/registration";
    }

    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("userForm") @Valid RegistrationRequest request, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "home/registration";
        }
        try {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%TU COś POWINNO SIę DZIAć$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            registrationService.register(request);
        }catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/error")
    public String getError(){
        return "error";
    }

    @GetMapping("/about")
    public String getAbout(ModelMap modelMap){
        modelMap.addAttribute("standardDate", new Date());
        return "home/about";
    }


    @GetMapping("/test")
    public String getTest(ModelMap modelMap,
                          HttpServletRequest request,
                          HttpServletResponse response){
        System.out.println("==============STRONA TESTOWA===================");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(authentication.getName());
        System.out.println(authentication.getPrincipal().toString());
        System.out.println(authentication.getDetails().toString());
        List<Country> countries = countryRepository.findAll();
//        countries.stream().forEach(System.out::println);
        List<CountryDtoForm> countryDtos = new ArrayList<>();
        for (Country country : countries) {
            countryDtos.add(new ModelMapper().map(country, CountryDtoForm.class));
        }
        countryDtos.stream()
                .filter(country -> country.getId() > 100)
                .filter(country -> country.getId() < 150)
                .filter(country -> country.getContinent().contains("Azja"))
                .map(country -> country.getCountryEn() + " - " + country.getCapital_city())
                .forEach(System.out::println);


//        ********************************************************
//        ***************TESTY AUTORYZACJI************************
//        ********************************************************

        System.out.println("++++++++++++++++++++++++++++NAPIS TESTOWY++++++++++++++++++++++++++++++++++");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(userCheckLoged.UserCheckLoged());
//        System.out.println(userCheckLoged.UserCheckLoged().getName());
//        System.out.println(userCheckLoged.UserCheckLoged().getAuthorities());
//        System.out.println(userCheckLoged.UserCheckLoged().getCredentials());
//        System.out.println(userCheckLoged.UserCheckLoged().getPrincipal().getClass().getCanonicalName());


        System.out.println("---------------------TEST START------------------------");
        System.out.println(request.toString());
        System.out.println(response.getLocale().toString());
        modelMap.addAttribute("ip", request.getRemoteAddr());


        System.out.println("=========================================");
        Locale currentLocale = request.getLocale();
        System.out.println(response.getLocale().getCountry() + " : " + response.getLocale().getDisplayCountry());
        System.out.println(currentLocale.getLanguage() + " : " + currentLocale.getDisplayLanguage());
//        System.out.println(request.getHeader("X-FORWARDED-FOR"));
        System.out.println(request.getRemoteAddr());
        System.out.println("==========================================");

        System.out.println(LocalDateTime.now());

        modelMap.addAttribute("standardDate", new Date());
        modelMap.addAttribute("localDateTime", LocalDateTime.now());
        modelMap.addAttribute("localDate", LocalDate.now());
        modelMap.addAttribute("timestamp", Instant.now());

        System.out.println("---------------------TEST STOP------------------------");

        return "home/test";
    }

    @GetMapping("/metal")
    public String getMetal(ModelMap modelMap){
        modelMap.addAttribute("standardDate", new Date());
        modelMap.addAttribute("metalRate","Metal Rate TEst");
        String url = "https://api.gold-api.com/symbols";
//        apiService.getMetalSymbol(url);

        GetMetalSymbol getMetalSymbol = apiService.getMetalSymbol(url);

        GetMetalRate getMetalRate = apiService.getMetalRate("https://api.gold-api.com/price/", getMetalSymbol);

        modelMap.addAttribute("metalPrice", getMetalRate);
        return "home/metal";
    }

    @GetMapping("/moneyMetal")
    public String getMonyeMetal(ModelMap modelMap) {

//        Long start = System.currentTimeMillis();
//        GetMoneyMetals getMoneyMetals = apiService.getMoneyMetal("https://www.moneymetals.com/api/spot-prices.json");
//        Long stop = System.currentTimeMillis();
//        System.out.println("++++++++++++++++++++++++++++++++++++++++++TIME");
//        System.out.println(stop - start);
//        System.out.println("-----------CZAS----------");
//        System.out.println("++++++++++++++++++++++++++++++++++++++++++TIME");
//        modelMap.addAttribute("rates", getMoneyMetals);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://www.moneymetals.com/api/spot-prices.json");

            // Dodaj nagłówki jak w prawdziwej przeglądarce
            request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            request.setHeader("Accept", "application/json");
            request.setHeader("Accept-Language", "en-US,en;q=0.9");
            request.setHeader("Referer", "https://www.moneymetals.com/");
            request.setHeader("Connection", "keep-alive");

            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                System.out.println("Dane JSON:\n" + json);
                JSONObject jsonObject = new JSONObject(json);
                System.out.println(JsonUtils.gsonPretty(jsonObject.getJSONObject("spot_prices")));
                System.out.println(JsonUtils.gsonPretty(jsonObject.getJSONObject("spot_prices").length()));
            } else {
                System.out.println("Błąd HTTP: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "home/moneyMetal";
    }
}
