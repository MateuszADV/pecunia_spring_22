package pecunia_22.models.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Security;

import java.sql.Date;
import java.util.List;

@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {

    @Query(value = "SELECT sec FROM Security sec ORDER BY sec.id")
    List<Security> getAllSecurityOrderById();

    @Query(value = "SELECT sec FROM Security sec " +
            "WHERE sec.currencies.id = ?1")
    List<Security> getSecurityByCurrencyId(Long currencyId);

    @Query(value = "SELECT new map(sec.qualities AS qualities, sec.makings AS makings, sec.id AS securityId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, cur.patterns AS patterns, bou.name AS bought, sec.denomination AS denomination, sec.nameCurrency AS nameCurrency, sec.itemDate AS itemDate, " +
            "sec.priceBuy AS priceBuy, sec.priceSell AS priceSell, sec.quantity AS quantity, sec.unitQuantity AS unitQuantity, " +
            "sec.width AS width, sec.height AS height, sec.visible AS visible, sec.description AS description, " +
            "sec.aversPath AS aversPath, sec.reversePath AS reversePath ) " +
            "  FROM Security sec" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Bought bou" +
            "    ON bou = sec.boughts" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = sec.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = sec.statuses" +
            " GROUP BY sec.qualities, sec.makings, sec.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, cur.patterns, bou.name, sec.denomination, sec.nameCurrency, sec.itemDate, " +
            "          sec.priceBuy, sec.priceSell, sec.quantity, sec.unitQuantity, sec.width, sec.height, sec.visible, sec.description, sec.aversPath, sec.reversePath " +
            " ORDER BY cou.countryEn, sec.denomination")
    List<Object[]> getSecuritiesByStatus(String status);

    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Security security" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = security.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = security.statuses" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status);

    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Security security" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = security.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = security.statuses AND security.visible = ?2" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status, Boolean visible);

    @Query(value = "SELECT new map(cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, COUNT(cur.currencySeries) AS total) " +
            "  FROM Security security" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = security.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = security.statuses AND cou.id = ?2" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, cur.currencySeries, cur.id" +
            " ORDER BY cur.currencySeries")
    List<Object[]> currencyByStatus(String status, Long countryId);

    @Query(value = "SELECT new map(cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, COUNT(cur.currencySeries) AS total) " +
            "  FROM Security security" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = security.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = security.statuses AND cou.id = ?2 AND security.visible = ?3" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, cur.currencySeries, cur.id" +
            " ORDER BY cur.currencySeries")
    List<Object[]> currencyByStatus(String status, Long countryId, Boolean visible);

    @Query(value = "SELECT security FROM Security security " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2 " +
            "WHERE security.currencies.id = ?1 AND stat = security.statuses " +
            "ORDER BY security.denomination")
    Page<Security> securityPageable(Long currencyId, String status, final Pageable pageable);

    @Query(value = "SELECT security FROM Security security " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2" +
            "WHERE security.currencies.id = ?1 AND stat = security.statuses  AND security.visible = ?3 " +
            "ORDER BY security.denomination")
    Page<Security> securityPageable(Long currencyId, String status, Boolean visible, final Pageable pageable);

    //    *************************
    //    ****SECURITIES UPDATE****
    //    **************************

    @Transactional
    @Modifying
    @Query(value = "update Security secu set secu.currencies.id = ?1, secu.denomination = ?2, secu.dateBuy = ?3, secu.nameCurrency = ?4, secu.series = ?5, " +
                    "secu.boughts.id = ?6, secu.itemDate = ?7, secu.quantity = ?8, secu.unitQuantity = ?9, secu.actives.id = ?10, secu.priceBuy = ?11, secu.priceSell = ?12, " +
                    "secu.makings.id = ?13, secu.qualities.id = ?14, secu.width = ?15, secu.height = ?16, secu.statuses.id = ?17, secu.imageTypes.id = ?18, " +
                    "secu.statusSell = ?19, secu.visible = ?20, secu.description = ?21, secu.aversPath = ?22, secu.reversePath = ?23, secu.serialNumber = ?24 "+
                    "where secu.id = ?25")
    void updateSecurity(Long currencyId, Double denomination, Date dateBuy, String nameCurrency, String series,
                    Long boughtsId, String itemDate, Integer quantity, String unitQuantity, Long activesId, Double priceBuy, Double priceSell,
                    Long making, Long quality, Integer width, Integer height, Long status, Long imageType,
                    String statusSell, Boolean visible, String  description, String aversePath, String ReversePath, String serialNumber,
                    Long securityId);
}
