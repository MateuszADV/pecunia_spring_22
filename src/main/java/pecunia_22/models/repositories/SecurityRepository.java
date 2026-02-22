package pecunia_22.models.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Security;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.sql.Date;
import java.util.List;

@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {

    @Query(value = "SELECT sec FROM Security sec ORDER BY sec.id")
    List<Security> getAllSecurityOrderById();

    @Query(value = "SELECT sec FROM Security sec " +
            "WHERE sec.currencies.id = ?1")
    List<Security> getSecurityByCurrencyId(Long currencyId);

    @Query("""
SELECT new map(
    cou.id              AS countryId,
    cou.countryEn       AS countryEn,
    cou.countryPl       AS countryPl,
    cur.id              AS currencyId,
    cur.currencySeries  AS currencySeries,
    cur.patterns        AS patterns,
    bou.name            AS bought,
    sec.denomination    AS denomination,
    sec.nameCurrency    AS nameCurrency,
    sec.itemDate        AS itemDate,
    sec.priceBuy        AS priceBuy,
    sec.priceSell       AS priceSell,
    sec.quantity        AS quantity,
    sec.unitQuantity    AS unitQuantity,
    sec.qualities       AS qualities,
    sec.makings         AS makings,
    sec.width           AS width,
    sec.height          AS height,
    sec.visible         AS visible,
    sec.description     AS description,
    sec.aversPath       AS aversPath,
    sec.reversePath     AS reversePath,
    sec.id              AS securityId
)
FROM Security sec
        JOIN sec.statuses stat
        JOIN sec.boughts bou
        JOIN sec.currencies cur
        JOIN cur.countries cou
WHERE stat.status = :status
  AND (:excludedStatusSell IS NULL OR sec.statusSell <> :excludedStatusSell)
  AND (:countryId IS NULL OR cou.id = :countryId)
ORDER BY cou.countryEn, sec.denomination
""")
    List<Object[]> getSecuritiesByStatus(
            @Param("status") String status,
            @Param("excludedStatusSell") String excludedStatusSell,
            @Param("countryId") Long countryId
    );

//    @Query(value = "SELECT new map(sec.qualities AS qualities, sec.makings AS makings, sec.id AS securityId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
//            "cur.currencySeries AS currencySeries, cur.patterns AS patterns, bou.name AS bought, sec.denomination AS denomination, sec.nameCurrency AS nameCurrency, sec.itemDate AS itemDate, " +
//            "sec.priceBuy AS priceBuy, sec.priceSell AS priceSell, sec.quantity AS quantity, sec.unitQuantity AS unitQuantity, " +
//            "sec.width AS width, sec.height AS height, sec.visible AS visible, sec.description AS description, " +
//            "sec.aversPath AS aversPath, sec.reversePath AS reversePath ) " +
//            "  FROM Security sec" +
//            "  LEFT JOIN Status stat" +
//            "    ON stat.status = ?1" +
//            "  LEFT JOIN Bought bou" +
//            "    ON bou = sec.boughts" +
//            "  LEFT JOIN Currency cur" +
//            "    ON cur = sec.currencies" +
//            "  LEFT JOIN Country cou" +
//            "    ON cou = cur.countries" +
//            " WHERE stat = sec.statuses" +
//            " GROUP BY sec.qualities, sec.makings, sec.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, cur.patterns, bou.name, sec.denomination, sec.nameCurrency, sec.itemDate, " +
//            "          sec.priceBuy, sec.priceSell, sec.quantity, sec.unitQuantity, sec.width, sec.height, sec.visible, sec.description, sec.aversPath, sec.reversePath " +
//            " ORDER BY cou.countryEn, sec.denomination")
//    List<Object[]> getSecuritiesByStatus(String status);

    /**
     * Zwraca statystyki krajów dla danego statusu papierów wartościowych.
     *
     * @param status   status security (np. KOLEKCJA, SPRZEDANE)
     * @param visible  opcjonalny filtr widoczności:
     *                 - true  → tylko widoczne
     *                 - false → tylko niewidoczne
     *                 - null  → bez filtra widoczności
     *
     * @return lista statystyk krajów (ilość security według kraju)
     */
    @Query("""
    SELECT new pecunia_22.models.sqlClass.CountryByStatus(
        con.continentEn,
        con.continentCode,
        cou.id,
        cou.countryEn,
        cou.countryPl,
        COUNT(security.id)
    )
    FROM Security security
        JOIN security.statuses stat
        JOIN security.currencies cur
        JOIN cur.countries cou
        JOIN cou.continents con
    WHERE stat.status = :status
      AND (:visible IS NULL OR security.visible = :visible)
    GROUP BY
        con.continentEn,
        con.continentCode,
        cou.id,
        cou.countryEn,
        cou.countryPl
    ORDER BY cou.countryEn
""")
    List<CountryByStatus> countryByStatus(
            @Param("status") String status,
            @Param("visible") Boolean visible
    );

    /**
     * Zwraca statystyki walut dla danego kraju i statusu papierów wartościowych.
     *
     * @param status     status (np. KOLEKCJA, NA_SPRZEDAZ)
     * @param countryId  identyfikator kraju
     * @param visible    opcjonalny filtr widoczności:
     *                   - true  → tylko widoczne
     *                   - false → tylko niewidoczne
     *                   - null  → bez filtra widoczności
     *
     * @return lista statystyk walut (ilość rekordów według serii waluty)
     */
    @Query("""
    SELECT new pecunia_22.models.sqlClass.CurrencyByStatus(
        cou.id,
        con.continentEn,
        cou.countryEn,
        cou.countryPl,
        cur.id,
        cur.currencySeries,
        COUNT(security.id)
    )
    FROM Security security
        JOIN security.statuses stat
        JOIN security.currencies cur
        JOIN cur.countries cou
        JOIN cou.continents con
    WHERE stat.status = :status
      AND cou.id = :countryId
      AND (:visible IS NULL OR security.visible = :visible)
    GROUP BY
        cou.id,
        con.continentEn,
        cou.countryEn,
        cou.countryPl,
        cur.id,
        cur.currencySeries
    ORDER BY cur.currencySeries
""")
    List<CurrencyByStatus> currencyByStatus(
            @Param("status") String status,
            @Param("countryId") Long countryId,
            @Param("visible") Boolean visible
    );


    @Query(value = "SELECT security FROM Security security " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2 " +
            "WHERE security.currencies.id = ?1 AND stat = security.statuses " +
            "ORDER BY security.denomination")
    Page<Security> securityPageable(Long currencyId, String status, final Pageable pageable);

    @Query(value = "SELECT security FROM Security security " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2 " +
            "WHERE security.currencies.id = ?1 AND stat = security.statuses  AND security.visible = ?3 " +
            "ORDER BY security.denomination")
    Page<Security> securityPageable(Long currencyId, String status, Boolean visible, final Pageable pageable);

    //    *************************
    //    ****SECURITIES UPDATE****
    //    **************************

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
        UPDATE Security s
        SET
            s.currencies.id = :#{#security.currencies.id},
            s.denomination = :#{#security.denomination},
            s.dateBuy = :#{#security.dateBuy},
            s.nameCurrency = :#{#security.nameCurrency},
            s.series = :#{#security.series},
            s.boughts.id = :#{#security.boughts.id},
            s.itemDate = :#{#security.itemDate},
            s.quantity = :#{#security.quantity},
            s.unitQuantity = :#{#security.unitQuantity},
            s.actives.id = :#{#security.actives.id},
            s.priceBuy = :#{#security.priceBuy},
            s.priceSell = :#{#security.priceSell},
            s.makings.id = :#{#security.makings.id},
            s.qualities.id = :#{#security.qualities.id},
            s.width = :#{#security.width},
            s.height = :#{#security.height},
            s.statuses.id = :#{#security.statuses.id},
            s.imageTypes.id = :#{#security.imageTypes.id},
            s.statusSell = :#{#security.statusSell},
            s.visible = :#{#security.visible},
            s.description = :#{#security.description},
            s.aversPath = :#{#security.aversPath},
            s.reversePath = :#{#security.reversePath},
            s.serialNumber = :#{#security.serialNumber}
        WHERE s.id = :#{#security.id}
    """)
    void updateSecurity(@Param("security") Security security);
}