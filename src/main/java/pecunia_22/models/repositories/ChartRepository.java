package pecunia_22.models.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ws.rs.client.Invocation;
import org.json.JSONArray;
import org.springframework.stereotype.Repository;
import pecunia_22.models.others.ApiResponseInfo;
import pecunia_22.services.apiService.ApiServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ChartRepository {

    private ApiServiceImpl apiService;

    public ChartRepository(ApiServiceImpl apiService) {
        this.apiService = apiService;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> my_report_continents_test() {
        Query query = entityManager.createQuery("SELECT con.continentEn, COUNT(CASE WHEN cou.continent IS NULL THEN 1 ELSE 0 END) FROM Country cou " +
                "LEFT JOIN Continent con " +
                "ON cou.continents = con " +
                "GROUP BY con.continentEn");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_note_currency_country(String country) {
        Query query = entityManager.createQuery("SELECT cur.currencySeries, COUNT(CASE WHEN note.currencies IS NULL THEN 1 ELSE 0 END) FROM Note note" +
                "  LEFT JOIN Currency cur" +
                "    ON note.currencies = cur" +
                "  LEFT JOIN Country  cou" +
                "    ON cou.countryEn = '" + country + "'" +
                " WHERE cou = cur.countries" +
                " GROUP BY cur.currencySeries");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_object_status() {
        Query query = entityManager.createQuery("SELECT stat.status, COUNT(CASE WHEN note.statuses IS NULL THEN 1 ELSE 0 END) FROM Note note" +
                "  LEFT JOIN Status stat" +
                "    ON stat = note.statuses" +
                " GROUP BY stat.status, note.statuses");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_coins_composition() {
        Query query = entityManager.createQuery("SELECT coin.composition, COUNT(CASE WHEN coin.composition IS NULL THEN 1 ELSE 0 END) FROM Coin coin" +
                " GROUP BY coin.composition" +
                " ORDER BY coin.composition DESC ");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_note_bought() {
        Query query = entityManager.createQuery("SELECT bou.name, COUNT(CASE WHEN note.boughts IS NULL THEN 1 ELSE 0 END) FROM Note note" +
                "  LEFT JOIN Bought bou" +
                "    ON bou = note.boughts" +
                " GROUP BY bou.name");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_notes_bought_country(String country) {
        Query query = entityManager.createQuery("SELECT bou.name, COUNT(CASE WHEN note.boughts IS NULL THEN 1 ELSE 0 END) FROM Note note" +
                "  LEFT JOIN Currency cur" +
                "    ON note.currencies = cur" +
                "  LEFT JOIN Country  cou" +
                "    ON cou = cur.countries" +
                "  LEFT JOIN Bought bou" +
                "    ON bou = note.boughts" +
                " WHERE cou.countryEn = '" + country + "'" +
                " GROUP BY bou.name");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_notes_add() {
        Query query = entityManager.createQuery("SELECT function('to_char', note.created_at, 'YYYY-MM'), COUNT(note.created_at) FROM Note note" +
                " GROUP BY function('to_char', note.created_at, 'YYYY-MM')  " +
                " ORDER BY function('to_char', note.created_at, 'YYYY-MM')");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_notes_buy() {
        Query query = entityManager.createQuery("SELECT function('to_char', note.dateBuy, 'YYYY-MM'), COUNT(note.dateBuy) FROM Note note" +
                " GROUP BY function('to_char', note.dateBuy, 'YYYY-MM')  " +
                " ORDER BY function('to_char', note.dateBuy, 'YYYY-MM')");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_coin_bought() {
        Query query = entityManager.createQuery("SELECT bou.name, COUNT(CASE WHEN coin.boughts IS NULL THEN 1 ELSE 0 END) FROM Coin coin" +
                "  LEFT JOIN Bought bou" +
                "    ON bou = coin.boughts" +
                " GROUP BY bou.name");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_coins_bought_country(String country) {
        Query query = entityManager.createQuery("SELECT bou.name, COUNT(CASE WHEN coin.boughts IS NULL THEN 1 ELSE 0 END) FROM Coin coin" +
                "  LEFT JOIN Currency cur" +
                "    ON coin.currencies = cur" +
                "  LEFT JOIN Country  cou" +
                "    ON cou = cur.countries" +
                "  LEFT JOIN Bought bou" +
                "    ON bou = coin.boughts" +
                " WHERE cou.countryEn = '" + country + "'"  +
                " GROUP BY bou.name");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_coins_add() {
        Query query = entityManager.createQuery("SELECT function('to_char', coin.created_at, 'YYYY-MM'), COUNT(coin.created_at) FROM Coin coin" +
                " GROUP BY function('to_char', coin.created_at, 'YYYY-MM')  " +
                " ORDER BY function('to_char', coin.created_at, 'YYYY-MM')");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_gold() {
        String url = "http://api.nbp.pl/api/cenyzlota/last/255";
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();
        List<Object[]> objects = new ArrayList<>();
        try {
            Invocation.Builder webResource = apiService.webResource(url);
            String stringJson = webResource.get(String.class);
            JSONArray jsonArray = new JSONArray(stringJson);
            apiResponseInfo.setResponseStatusInfo(webResource.accept("application/json").get().getStatusInfo());
            System.out.println(JsonUtils.gsonPretty(apiResponseInfo));
//            System.out.println(JsonUtils.gsonPretty(jsonArray));

            for (int i = 0; i < jsonArray.length(); i++) {
                Object[] object = {jsonArray.getJSONObject(i).get("data"),
                        jsonArray.getJSONObject(i).getDouble("cena") *  31.1034768};
                objects.add(object);
            }

            System.out.println((objects.size()));
//            System.out.println(JsonUtils.gsonPretty(objects));
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            return objects;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return objects;
    }

    public List<Object[]> my_report_test() {
        Query query = entityManager.createQuery("SELECT mak.making," +
                "  SUM(CASE stat.status WHEN 'KOLEKCJA' THEN 1 ELSE 0 END) AS KOLEKCJA," +
                "  SUM(CASE stat.status WHEN 'FOR SELL' THEN 1 ELSE 0 END) AS FOR_SELL," +
                "  SUM(CASE stat.status WHEN 'SOLD' THEN 1 ELSE 0 END) AS SOLD," +
                "  SUM(CASE stat.status WHEN 'NEW' THEN 1 ELSE 0 END) AS NEW," +
                "  SUM(CASE stat.status WHEN 'FUTURE' THEN 1 ELSE 0 END) AS FUTURE," +
                "  SUM(CASE stat.status WHEN 'OTHER' THEN 1 ELSE 0 END) AS OTHER" +
                " FROM Note note" +
                " LEFT JOIN Making mak" +
                "   ON note.makings = mak" +
                " LEFT JOIN Status stat" +
                "   ON note.statuses = stat " +
                "WHERE mak.making IN ('Papier', 'Polimer', 'Hybryda', 'Other' )" +
                "GROUP BY mak.making " +
                "ORDER BY mak.making DESC");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

    public List<Object[]> my_report_test2() {
        Query query = entityManager.createQuery("SELECT qual.quality," +
                "  SUM(CASE stat.status WHEN 'KOLEKCJA' THEN 1 ELSE 0 END) AS KOLEKCJA," +
                "  SUM(CASE stat.status WHEN 'FOR SELL' THEN 1 ELSE 0 END) AS FOR_SELL," +
                "  SUM(CASE stat.status WHEN 'SOLD' THEN 1 ELSE 0 END) AS SOLD," +
                "  SUM(CASE stat.status WHEN 'NEW' THEN 1 ELSE 0 END) AS NEW," +
                "  SUM(CASE stat.status WHEN 'FUTURE' THEN 1 ELSE 0 END) AS FUTURE," +
                "  SUM(CASE stat.status WHEN 'OTHER' THEN 1 ELSE 0 END) AS OTHER" +
                " FROM Note note" +
                " LEFT JOIN Quality qual" +
                " ON note.qualities = qual" +
                " LEFT JOIN Status stat" +
                "   ON note.statuses = stat " +
                "WHERE qual.quality IN (SELECT qua.quality FROM Quality qua)" +
                "GROUP BY qual.quality ");
//                "ORDER BY mak.making DESC");
        List<Object[]> objects = query.getResultList();
        return objects;
    }

}
