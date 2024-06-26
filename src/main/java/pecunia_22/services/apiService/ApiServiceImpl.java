package pecunia_22.services.apiService;


import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import org.apache.catalina.WebResource;
import org.glassfish.jersey.client.ClientResponse;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import pecunia_22.models.others.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {
    @Override
    public ClientResponse clientResponse(String url) {

        Client client = ClientBuilder.newClient();
        ClientResponse clientResponse = null;

       return clientResponse;
//        Client client = Client.create();
//        WebResource webResource = client.resource(url);
//        ClientResponse clientResponse = webResource.accept("application/json")
//                .get(ClientResponse.class);
//
//        if (clientResponse.getStatus() == 200) {
//            client.destroy();
//            return clientResponse;
//        }
//        else{
//            throw new RuntimeException("Błąd pobrania... " + clientResponse.getStatusInfo() + " - " + clientResponse.getStatus());
//        }
    }

    @Override
    public GetApiMetal getApiMetal(String url) {
        GetApiMetal getApiMetal = new GetApiMetal();
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();
        List<ApiMetal> apiMetals = new ArrayList<>();

//        try {
//            ClientResponse clientResponse = clientResponse(url);
//            String stringJson = clientResponse.getEntity(String.class);
//            JSONArray jsonArray = new JSONArray(stringJson);
//
//            apiResponseInfo.setResponseStatusInfo(clientResponse.getStatusInfo());
//            getApiMetal.setApiResponseInfo(apiResponseInfo);
//
//            String metal;
//            Float price;
//            for(int i = 0; i < jsonArray.length(); i++) {
//                if ((jsonArray.getJSONObject(i).names().get(0).toString()).contains("timestamp")) {
//                    Long timeStamp = (Long) jsonArray.getJSONObject(i).get("timestamp");
//                    Timestamp stamp = new Timestamp(timeStamp);
//                    Date date = new Date(stamp.getTime());
//                    apiResponseInfo.setDate(date);
//                } else {
//                    metal = jsonArray.getJSONObject(i).names().get(0).toString();
//                    price = Float.parseFloat(jsonArray.getJSONObject(i).get(metal).toString());
//                    apiMetals.add(new ApiMetal(metal, price));
//                }
//            }
//            getApiMetal.setApiMetals(apiMetals);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        return getApiMetal;
    }

    @Override
    public Invocation.Builder webResource(String url) {
        Client client = ClientBuilder.newClient();
        Invocation.Builder webResource = client.target(url).request();

        if (webResource.get().getStatus() == 200) {
            return webResource;
        }
        else{
            throw new RuntimeException("Błąd pobrania... " + webResource.get().getStatusInfo() + " - " + webResource.get().getStatus());
        }
    }

    @Override
    public GetRateCurrencyTableA getRateCurrencyTableA(String url, String[] codes) {
        GetRateCurrencyTableA getRateCurrencyTableA = new GetRateCurrencyTableA();
        Exchange exchange = new Exchange();
        List<Rate> rates = new ArrayList<>();
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();

        try {
//            Client client = ClientBuilder.newClient();
//            Invocation.Builder webResource = client.target(url).request();
            Invocation.Builder webResource = webResource(url);

//            ClientResponse clientResponse = clientResponse(url);
//            String stringJson = clientResponse.getEntity(String.class);
            String stringJson = webResource.get(String.class);
            JSONArray jsonArray = new JSONArray(stringJson);

            String startDate = jsonArray.getJSONObject(0).get("effectiveDate").toString();
            java.sql.Date date = java.sql.Date.valueOf(startDate);

//            apiResponseInfo.setResponseStatusInfo(clientResponse.getStatusInfo());
            apiResponseInfo.setResponseStatusInfo(webResource.accept("application/json").get().getStatusInfo());
            apiResponseInfo.setDate(date);

            exchange.setNo(jsonArray.getJSONObject(0).get("no").toString());
            exchange.setTable(jsonArray.getJSONObject(0).get("table").toString());
            exchange.setEffectiveDate(jsonArray.getJSONObject(0).get("effectiveDate").toString());
            JSONArray jsonArray1 = new JSONArray(jsonArray.getJSONObject(0).getJSONArray("rates"));

            String code;
            for (int i = 0; i < jsonArray1.length(); i++) {
                code = jsonArray1.getJSONObject(i).get("code").toString();
                if (Arrays.stream(codes).anyMatch(code::equals)) {
                    Rate rate = new Rate();
                    rate.setCod(jsonArray1.getJSONObject(i).getString("code"));
                    rate.setCurrency(jsonArray1.getJSONObject(i).getString("currency"));
                    rate.setMid(jsonArray1.getJSONObject(i).getDouble("mid"));
                    rates.add(rate);
                }
            }
            getRateCurrencyTableA.setExchange(exchange);
            getRateCurrencyTableA.getExchange().setRates(rates);
            getRateCurrencyTableA.setApiResponseInfo(apiResponseInfo);
        }catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return getRateCurrencyTableA;
    }
}
