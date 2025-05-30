package pecunia_22.models.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Medal;

import java.sql.Date;
import java.util.List;

@Repository
public interface MedalRepository extends JpaRepository<Medal, Long> {
    @Query(value = "SELECT medal FROM Medal medal " +
            " WHERE medal.currencies.id = ?1")
    List<Medal> getMedalByCurrencyId(Long currencyId);

    @Query(value = "SELECT medal FROM Medal medal " +
            " WHERE medal.currencies.id = ?1 AND medal.visible = ?2")
    List<Medal> getMedalByCurrencyId(Long currencyId, Boolean visible);

    @Transactional
    @Modifying
    @Query(value = "update Medal medal set     medal.currencies.id = ?1, medal.denomination = ?2, medal.dateBuy = ?3,      medal.nameCurrency = ?4, medal.series = ?5, " +
            "medal.boughts.id = ?6,    medal.itemDate = ?7,      medal.quantity = ?8,     medal.unitQuantity = ?9, medal.actives.id = ?10,  medal.priceBuy = ?11, medal.priceSell = ?12, " +
            "medal.qualities.id = ?13, medal.diameter = ?14,     medal.thickness = ?15,   medal.weight = ?16,      medal.statuses.id = ?17, medal.imageTypes.id = ?18, " +
            "medal.statusSell = ?19,   medal.visible = ?20,      medal.composition = ?21, medal.description = ?22, medal.aversPath = ?23,   medal.reversePath = ?24 "+
            "where medal.id = ?25")
    void updateMedal(Long currencyId, Double denomination, Date dateBuy, String nameCurrency, String series,
                    Long boughtsId, String itemDate, Integer quantity, String unitQuantity, Long activesId, Double priceBuy, Double priceSell,
                    Long quality, Double diameter, Double thickness, Double weight, Long status, Long imageType,
                    String statusSell, Boolean visible, String composition, String  description, String aversePath, String ReversePath,
                    Long medalId);

    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Medal medal" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = medal.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = medal.statuses" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status);

    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Medal medal" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = medal.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = medal.statuses AND medal.visible = ?2" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status, Boolean visible);

    @Query(value = "SELECT new map(cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, COUNT(cur.currencySeries) AS total) " +
            "  FROM Medal medal" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = medal.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = medal.statuses AND cou.id = ?2" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, cur.currencySeries, cur.id" +
            " ORDER BY cur.currencySeries")
    List<Object[]> currencyByStatus(String status, Long countryId);

    @Query(value = "SELECT new map(cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, COUNT(cur.currencySeries) AS total) " +
            "  FROM Medal medal" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = medal.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = medal.statuses AND cou.id = ?2 AND medal.visible = ?3" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, cur.currencySeries, cur.id" +
            " ORDER BY cur.currencySeries")
    List<Object[]> currencyByStatus(String status, Long countryId, Boolean visible);

    @Query(value = "SELECT medal FROM Medal medal " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2 " +
            "WHERE medal.currencies.id = ?1 AND stat = medal.statuses " +
            "ORDER BY medal.denomination")
    Page<Medal> medalPageable(Long currencyId, String status, final Pageable pageable);

    @Query(value = "SELECT medal FROM Medal medal " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2" +
            "WHERE medal.currencies.id = ?1 AND stat = medal.statuses  AND medal.visible = ?3 " +
            "ORDER BY medal.denomination")
    Page<Medal> medalPageable(Long currencyId, String status, Boolean visible, final Pageable pageable);
}
