package pecunia_22.controllers.viewControllers;

import jakarta.ws.rs.client.Invocation;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pecunia_22.services.apiService.ApiServiceImpl;

@ControllerAdvice
@NoArgsConstructor
public class MvcAdvice {

    private ApiServiceImpl apiService;

    @Autowired
    public MvcAdvice(ApiServiceImpl apiService) {
        this.apiService = apiService;
    }

    @ModelAttribute
    public void addRateMetal(ModelMap modelMap) {
//       GetApiMetal getApiMetal = apiService.getApiMetal("https://api.metals.live/v1/spot");
//       modelMap.addAttribute("getApiMetal", getApiMetal);
        modelMap.addAttribute("getApiMetal", null);
//        System.out.println(JsonUtils.gsonPretty(getApiMetal));
    }

    @ModelAttribute
    public void currencyRAte() {
//        ClientResponse clientResponse = apiService.clientResponse("https://api.nbp.pl/api/exchangerates/tables/A/?format=json");
//        String stringJson = clientResponse.getEntity(String.class);
//        JSONArray jsonArray = new JSONArray(stringJson);
//        System.out.println(clientResponse.getHeaders());
//        System.out.println(JsonUtils.gsonPretty(jsonArray.getJSONObject(0).get("table")));
//        System.out.println(JsonUtils.gsonPretty(jsonArray.getJSONObject(0).get("no")));
//        System.out.println(JsonUtils.gsonPretty(jsonArray.getJSONObject(0).get("effectiveDate")));
//        System.out.println(JsonUtils.gsonPretty(jsonArray.getJSONObject(0).getJSONArray("rates").getJSONObject(0)));
    }

    @ModelAttribute
    public void metalRate(ModelMap modelMap) {

        System.out.println("TEST METAL RATE");
    }
}
