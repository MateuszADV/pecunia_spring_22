package pecunia_22.services.apiService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import org.glassfish.jersey.client.ClientResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pecunia_22.models.dto.seting.SettingDto;
import pecunia_22.models.others.*;
import pecunia_22.models.others.NBP.*;
import pecunia_22.models.others.moneyMetals.GetMoneyMetals;
import pecunia_22.models.others.moneyMetals.MoneyMetal;
import pecunia_22.models.setting.Setting;
import pecunia_22.services.setingService.SettingServiceImpl;
import utils.JsonUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@AllArgsConstructor
public class ApiServiceImpl implements ApiService {

    private SettingServiceImpl settingService;

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
//        System.out.println(webResource.get().getHeaders().toString());
        if (webResource.get().getStatus() == 200) {
            return webResource;
        }
        else{
            throw new RuntimeException("Błąd pobrania... " + webResource.get().getStatusInfo() + " - " + webResource.get().getStatus());
        }
    }

    @Override
    public GetRateCurrencyTableA getRateCurrencyTableA(String url, String[] codes) {
        Long startNBP = System.currentTimeMillis();
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
                if (Arrays.asList(codes).contains(code)) {
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
            Long stopNBP = System.currentTimeMillis();
            System.out.println(stopNBP-startNBP);
        }catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return getRateCurrencyTableA;
    }

    @Override
    public GetMetalSymbol getMetalSymbol(String url) {
        List<MetalSymbol> getSymbols = new ArrayList<>();
        GetMetalSymbol getMetalSymbol = new GetMetalSymbol();
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();
        try {
            Invocation.Builder webResource = webResource(url);
            String stringJson = webResource.get(String.class);
            JSONArray jsonArray = new JSONArray(stringJson);
            System.out.println(JsonUtils.gsonPretty(jsonArray));
            System.out.println(jsonArray.length());
            System.out.println(jsonArray.getJSONObject(0).get("symbol").toString());

            apiResponseInfo.setResponseStatusInfo(webResource.accept("application/json").get().getStatusInfo());
            System.out.println(webResource.accept("application/json").get().getStatusInfo());


            for (int i = 0; i < jsonArray.length(); i++) {
                MetalSymbol getSymbol = new MetalSymbol();
                getSymbol.setSymbol(jsonArray.getJSONObject(i).get("symbol").toString());
                getSymbol.setName(jsonArray.getJSONObject(i).get("name").toString());
                getSymbols.add(getSymbol);
            }

            getMetalSymbol.setMetalSymbols(getSymbols);
            getMetalSymbol.setApiResponseInfo(apiResponseInfo);
            System.out.println(JsonUtils.gsonPretty(getMetalSymbol));
            return getMetalSymbol;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public GetMetalRate getMetalRate(String url, GetMetalSymbol getMetalSymbol) {
        Long startA = System.currentTimeMillis();
        System.out.println(url);
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();
        GetMetalRate getMetalRate = new GetMetalRate();
        List<MetalRate> metalRates = new ArrayList<>();
//        System.out.println(JsonUtils.gsonPretty(getMetalSymbol));
        Invocation.Builder webResource = null;
        try {
            for (MetalSymbol metalSymbol : getMetalSymbol.getMetalSymbols()) {
                Long start = System.currentTimeMillis();
                webResource = webResource(url + metalSymbol.getSymbol());

                String stringJson = webResource.get(String.class);
                JSONObject jsonObject = new JSONObject(stringJson);
//                System.out.println(JsonUtils.gsonPretty(jsonObject));

                MetalRate metalRate = new MetalRate();
                metalRate.setSymbol(jsonObject.getString("symbol"));
                metalRate.setName(jsonObject.getString("name"));
                metalRate.setPrice(jsonObject.getFloat("price"));
                metalRate.setUpdateAt((jsonObject.getString("updatedAt").formatted()));
                metalRate.setUpdatedAtReadable(jsonObject.getString("updatedAtReadable"));

                metalRates.add(metalRate);
                Long stop = System.currentTimeMillis();
                System.out.println("++++++++++++++++++++++++++++++++++++++++++TIME");
                System.out.println(stop - start);
                System.out.println( " - - - CZAS - - -  ");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++TIME");
            }

            assert webResource != null;
            apiResponseInfo.setResponseStatusInfo(webResource.accept("application/json").get().getStatusInfo());
            getMetalRate.setApiResponseInfo(apiResponseInfo);
            getMetalRate.setMetalRates(metalRates);

            metalRate(getMetalRate);
            Long stopA = System.currentTimeMillis();
            System.out.println("++++++++++++++++++++++++++++++++++++++++++TIME");
            System.out.println(stopA - startA);
            System.out.println("CZAS-A");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++TIME");
            return getMetalRate;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void metalRate(GetMetalRate getMetalRate) {
        Long startB = System.currentTimeMillis();
        System.out.println(JsonUtils.gsonPretty(getMetalRate.getApiResponseInfo().getResponseStatusInfo().toString()));

        if (Objects.equals(getMetalRate.getApiResponseInfo().getResponseStatusInfo().toString(), "OK")) {
            System.out.println("_________________________________________________________________________________________________________________________");
            System.out.printf("|- %-10s | %-10s | %-15s | %-40s | %-30s |%n", "Symbol", "Name", "Price", "UpdatedAt", "updatedAtReadable");
            for (MetalRate metalRate : getMetalRate.getMetalRates()) {
                System.out.println("|------------------------------------------------------------------------------------------------------------------------|");
                System.out.printf("|- %-10s | %-10s | %-15s | %-40s | %-30s |%n", metalRate.getSymbol(), metalRate.getName(), metalRate.getPrice(), metalRate.getUpdateAt(), metalRate.getUpdatedAtReadable());
            }
            System.out.println("|------------------------------------------------------------------------------------------------------------------------|");
        }
        Long stopB = System.currentTimeMillis();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++TIME");
        System.out.println(stopB - startB);
        System.out.println("CZAS-B");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++TIME");
    }

    @Override
    public GetMoneyMetals getMoneyMetal(String url) {
        final String entity = getJsonString();

        GetMoneyMetals getMoneyMetals = new GetMoneyMetals();
        List<MoneyMetal> moneyMetals = new ArrayList<>();

        try {
            String json = entity.toString();
            JSONObject jsonObject = new JSONObject(json);
            System.out.println(JsonUtils.gsonPretty(jsonObject));

            Long milliSec = jsonObject.getLong("time");

//            System.out.println(jsonObject.keys());
//            System.out.println(jsonObject.getJSONObject("spot_prices").toMap());
            Map<String, Object> rate = jsonObject.getJSONObject("spot_prices").toMap();

//            for (String key : rate.keySet()) {
//                System.out.println(key + ":" + rate.get(key));
//            }
            Date res = new Date(milliSec);

            rate.forEach((k, v) -> {
                moneyMetals.add(new MoneyMetal(k, Float.parseFloat(v.toString())));
            });

            getMoneyMetals.setTime(res);
            getMoneyMetals.setMoneyMetalList(moneyMetals);

            moneyMetal(rate);
            return  getMoneyMetals;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static String getJsonString() {
        try {
            Client client = ClientBuilder.newClient();
            String entity = client.target("https://www.moneymetals.com/api/spot-prices.json")
                    .request(MediaType.APPLICATION_JSON).header("application/json", "true").get(String.class);
            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void moneyMetal(Map<String, Object> rate) {
        System.out.printf("|- %-10s | %-10s |%n", "Name", "Price");
        System.out.println("|--------------------------|");
//        rate.forEach((k, v) -> System.out.printf("\u001B[33m" + "|- %-10s | %10s |%n", k, v));
        rate.forEach((k, v) -> {
            if (k.equals("gold")) {
                System.out.printf("\u001B[33m" + "|- %-10s | %-10s |%n", k, v);
            } else
                System.out.printf("\u001B[0m" + "|- %-10s | %-10s |%n", k, v);
        });

        System.out.println("|--------------------------|");

    }

    @Override
    public List<GetGoldRateNBP> getGoldRateNBP(String url) {

        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();
        List<GetGoldRateNBP>getGoldRateNBPList = new ArrayList<>();
        try {
            Invocation.Builder webResource = webResource(url);
            String stringJson = webResource.get(String.class);
            JSONArray jsonArray = new JSONArray(stringJson);
            apiResponseInfo.setResponseStatusInfo(webResource.accept("application/json").get().getStatusInfo());
            System.out.println(JsonUtils.gsonPretty(apiResponseInfo));
//            System.out.println(JsonUtils.gsonPretty(jsonArray));

            for (int i = 0; i < jsonArray.length(); i++) {
                GetGoldRateNBP getGoldRateNBP = new GetGoldRateNBP();
                getGoldRateNBP.setDate(jsonArray.getJSONObject(i).get("data").toString());
                getGoldRateNBP.setPrice(jsonArray.getJSONObject(i).getDouble("cena") * 31.1034768);

                getGoldRateNBPList.add(getGoldRateNBP);
            }

            System.out.println(getGoldRateNBPList.size());
//            System.out.println(JsonUtils.gsonPretty(getGoldRateNBPList));
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            for(int i = 1; getGoldRateNBPList.size() > i; i++) {
                System.out.print(i + "   ");
                double change = ((getGoldRateNBPList.get(i).getPrice() / getGoldRateNBPList.get(i - 1).getPrice()) -1);
                System.out.println(change);
                getGoldRateNBPList.get(i).setChange(change);
            }
            return getGoldRateNBPList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public List<PriceStatistics>PriceStatistics(String apiUrl, Integer quantityDays) {
//        String apiUrl = "http://api.nbp.pl/api/cenyzlota/last/255";
        List<Double> prices = new ArrayList<>();
        Map<Double, String> priceDateMap = new HashMap<>();

        List<PriceStatistics> priceStatisticsList = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + quantityDays))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);
            for (JsonNode node : rootNode) {
                double price = node.get("cena").asDouble() * 31.1034768;
                String date = node.get("data").asText();
                prices.add(price);
                priceDateMap.put(price, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!prices.isEmpty()) {
            double sum = prices.stream().mapToDouble(Double::doubleValue).sum();
            double average = sum / prices.size();
            double median = calculateMedian(prices);
            double min = Collections.min(prices);
            double max = Collections.max(prices);

            priceStatisticsList.add(new PriceStatistics("average", average));
            System.out.println("Average price: " + average);
            priceStatisticsList.add(new PriceStatistics("median", median));
            System.out.println("Median price: " + median);
            priceStatisticsList.add(new PriceStatistics("min", min, priceDateMap.get(min)));
            System.out.println("Minimum price: " + min + " on " + priceDateMap.get(min));
            priceStatisticsList.add(new PriceStatistics("max", max, priceDateMap.get(max)));
            System.out.println("Maximum price: " + max + " on " + priceDateMap.get(max));
        } else {
            System.out.println("No data available.");
        }

        return priceStatisticsList;
    }

    private static double calculateMedian(List<Double> prices) {
        Collections.sort(prices);
        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            return prices.get(size / 2);
        }
    }

    @Override
    public ExchangeCurrency exchangeCurrency(String table, String cod) {
        List<RateCurrency> rateCurrencies = new ArrayList<>();
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();
        ExchangeCurrency exchangeCurrency = new ExchangeCurrency();
        try {
            Invocation.Builder webResource = webResource("https://api.nbp.pl/api/exchangerates/rates/" + table + "/" +cod + "/last/50/?format=json");
            apiResponseInfo.setResponseStatusInfo(webResource.accept("application/json").get().getStatusInfo());
            System.out.println(JsonUtils.gsonPretty(apiResponseInfo));
            String stringJson = webResource.get(String.class);
            JSONObject jsonObject = new JSONObject(stringJson);
            JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("rates"));

            for (int i = 0; jsonArray.length() > i; i++) {
                RateCurrency rateCurrency = new RateCurrency();
                rateCurrency.setEffectiveDate(jsonArray.getJSONObject(i).getString("effectiveDate"));
                if (table.equalsIgnoreCase("C")) {
                    rateCurrency.setAsk(jsonArray.getJSONObject(i).getDouble("ask"));
                    rateCurrency.setBid(jsonArray.getJSONObject(i).getDouble("bid"));
                } else {
                    rateCurrency.setMid(jsonArray.getJSONObject(i).getDouble("mid"));
                }
                rateCurrencies.add(rateCurrency);
            }
            exchangeCurrency.setCurrency(jsonObject.getString("currency"));
            exchangeCurrency.setCode(jsonObject.getString("code"));
            exchangeCurrency.setRateCurrencies(rateCurrencies);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        System.out.println(JsonUtils.gsonPretty(exchangeCurrency));
        return exchangeCurrency;
    }

    @Override
    public GetRateCurrency getRateCurrency(String table) {
        System.out.println("+++++++++++++++++++++++++++++++++PARAMETER START+++++++++++++++++++++++++");
        Setting setting = settingService.getSettingByName("rate_code");
        SettingDto settingDto = new ModelMapper().map(setting, SettingDto.class);
        System.out.println(JsonUtils.gsonPretty(settingDto));
        String[] rare_code = settingDto.getSetting().split(", ");
        for (String s : rare_code) {
            System.out.println(s);
        }
        System.out.println("+++++++++++++++++++++++++++++++++PARAMETER STOP+++++++++++++++++++++++++");

        GetRateCurrency getRateCurrency = new GetRateCurrency();
        List<Exchange> exchangeList = new ArrayList<>();
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();
        try {
            Invocation.Builder webResource = webResource("https://api.nbp.pl/api/exchangerates/tables/" + table + "/last/10?format=json");
            apiResponseInfo.setResponseStatusInfo(webResource.accept("application/json").get().getStatusInfo());
            String stringJson = webResource.get(String.class);
            JSONArray jsonArray  = new JSONArray(stringJson);

            for (int i = 0; jsonArray.length() > i; i++) {
                Exchange exchange = new Exchange();
                exchange.setTable(jsonArray.getJSONObject(i).getString("table"));
                exchange.setNo(jsonArray.getJSONObject(i).getString("no"));
                exchange.setEffectiveDate(jsonArray.getJSONObject(i).getString("effectiveDate"));

                JSONArray jsonRates = jsonArray.getJSONObject(i).getJSONArray("rates");
                List<Rate> rates = new ArrayList<>();
                for (int j = 0; j < jsonRates.length(); j++) {
                    String code = jsonRates.getJSONObject(j).getString("code");

                    Rate rate = new Rate();
//                    if (code.equals("EUR") || code.equals("USD")) {
                    for (String s : rare_code) {
                        if (code.equals(s)) {
                            rate.setMid(jsonRates.getJSONObject(j).getDouble("mid"));
                            rate.setCod(jsonRates.getJSONObject(j).getString("code"));
                            rate.setCurrency(jsonRates.getJSONObject(j).getString("currency"));
                            rates.add(rate);
                        }
                    }
                    exchange.setRates(rates);
                }
                exchangeList.add(exchange);
            }
            getRateCurrency.setExchangeList(exchangeList);
            getRateCurrency.setApiResponseInfo(apiResponseInfo);
//            System.out.println(JsonUtils.gsonPretty(getRateCurrency));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return getRateCurrency;
    }
}
