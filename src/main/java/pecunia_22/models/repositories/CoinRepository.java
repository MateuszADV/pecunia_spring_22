package pecunia_22.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Coin;

import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {

    @Query(value = "SELECT coin FROM Coin coin " +
            " WHERE coin.currencies.id = ?1")
    List<Coin> getCoinByCurrencyId(Long currencyId);

    @Query(value = "SELECT coin FROM Coin coin " +
            " WHERE coin.currencies.id = ?1 AND coin.visible = ?2")
    List<Coin> getCoinByCurrencyId(Long currencyId, Boolean visible);

    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Coin coin" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = coin.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = coin.statuses" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status);

    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Coin coin" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = coin.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = coin.statuses AND coin.visible = ?2" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status, Boolean visible);

    @Query(value = "SELECT new map(cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, COUNT(cur.currencySeries) AS total) " +
            "  FROM Coin coin" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = coin.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = coin.statuses AND cou.id = ?2" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, cur.currencySeries, cur.id" +
            " ORDER BY cur.currencySeries")
    List<Object[]> currencyByStatus(String status, Long countryId);

    @Query(value = "SELECT new map(cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, COUNT(cur.currencySeries) AS total) " +
            "  FROM Coin coin" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = coin.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = coin.statuses AND cou.id = ?2 AND coin.visible = ?3" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, cur.currencySeries, cur.id" +
            " ORDER BY cur.currencySeries")
    List<Object[]> currencyByStatus(String status, Long countryId, Boolean visible);

    @Query(value = "SELECT coin FROM Coin coin " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2 " +
            "WHERE coin.currencies.id = ?1 AND stat = coin.statuses " +
            "ORDER BY coin.denomination")
    Page<Coin> coinPageable(Long currencyId, String status, final Pageable pageable);

    @Query(value = "SELECT coin FROM Coin coin " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2" +
            "WHERE coin.currencies.id = ?1 AND stat = coin.statuses  AND coin.visible = ?3 " +
            "ORDER BY coin.denomination")
    Page<Coin> coinPageable(Long currencyId, String status, Boolean visible, final Pageable pageable);

    @Query(value = "SELECT new map(coin.qualities AS qualities, coin.id AS coinId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, cur.patterns AS patterns, bou.name AS bought, coin.denomination AS denomination, coin.nameCurrency AS nameCurrency, coin.itemDate AS itemDate, " +
            "coin.priceBuy AS priceBuy, coin.priceSell AS priceSell, coin.quantity AS quantity, coin.unitQuantity AS unitQuantity, " +
            "coin.diameter AS diameter, coin.thickness AS thickness, coin.weight AS weight, " +
            "coin.visible AS visible, coin.composition AS composition, coin.description AS description, " +
            "coin.aversPath AS aversPath, coin.reversePath AS reversePath ) " +
            "  FROM Coin coin" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Bought bou" +
            "    ON bou = coin.boughts" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = coin.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Quality qua" +
            "    ON qua = coin.qualities" +
            " WHERE stat = coin.statuses" +
            " GROUP BY coin.qualities, coin.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, cur.patterns, bou.name, coin.denomination, coin.nameCurrency, coin.itemDate, " +
            "          coin.priceBuy, coin.priceSell, coin.quantity, coin.unitQuantity, coin.visible, coin.description, coin.aversPath, coin.reversePath " +
            " ORDER BY cou.countryEn, coin.denomination")
    List<Object[]> getCoinsByStatus(String status);

    @Query(value = "SELECT new map(coin.qualities AS qualities, coin.id AS coinId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, cur.patterns AS patterns, bou.name AS bought, coin.denomination AS denomination, coin.nameCurrency AS nameCurrency, coin.itemDate AS itemDate, " +
            "coin.priceBuy AS priceBuy, coin.priceSell AS priceSell, coin.quantity AS quantity, coin.unitQuantity AS unitQuantity, " +
            "coin.diameter AS diameter, coin.thickness AS thickness, coin.weight AS weight, " +
            "coin.visible AS visible, coin.composition AS composition, coin.description AS description, " +
            "coin.aversPath AS aversPath, coin.reversePath AS reversePath ) " +
            "  FROM Coin coin" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Bought bou" +
            "    ON bou = coin.boughts" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = coin.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Quality qua" +
            "    ON qua = coin.qualities" +
            " WHERE stat = coin.statuses AND coin.statusSell != ?2 " +
            " GROUP BY coin.qualities, coin.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, cur.patterns, bou.name, coin.denomination, coin.nameCurrency, coin.itemDate, " +
            "          coin.priceBuy, coin.priceSell, coin.quantity, coin.unitQuantity, coin.visible, coin.description, coin.aversPath, coin.reversePath " +
            " ORDER BY cou.countryEn, coin.denomination")
    List<Object[]> getCoinsByStatus(String status, String statusSell);

    @Query(value = "SELECT new map(coin.qualities AS qualities, coin.id AS coinId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, bou.name AS bought, coin.denomination AS denomination, coin.nameCurrency AS nameCurrency, coin.itemDate AS itemDate, " +
            "coin.priceBuy AS priceBuy, coin.priceSell AS priceSell, coin.quantity AS quantity, coin.unitQuantity AS unitQuantity, " +
            "coin.diameter AS diameter, coin.thickness AS thickness, coin.weight AS weight, " +
            "coin.visible AS visible, coin.composition AS composition, coin.description AS description, " +
            "coin.aversPath AS aversPath, coin.reversePath AS reversePath ) " +
            "  FROM Coin coin" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Bought bou" +
            "    ON bou = coin.boughts" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = coin.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Quality qua" +
            "    ON qua = coin.qualities" +
            " WHERE stat = coin.statuses AND cou.id = ?2" +
            " GROUP BY coin.qualities, coin.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, bou.name, coin.denomination, coin.nameCurrency, coin.itemDate, " +
            "          coin.priceBuy, coin.priceSell, coin.quantity, coin.unitQuantity, coin.visible, coin.description, coin.aversPath, coin.reversePath " +
            " ORDER BY cou.countryEn, coin.denomination")
    List<Object[]> getCoinsByStatus(String status, Long countrtyId);
}
