package pecunia_22.models.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Note;
import pecunia_22.models.Security;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.sql.Date;
import java.util.List;

@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {

    @Query("""
    SELECT sec FROM Security sec 
    ORDER BY sec.id
    """)
    List<Security> getAllSecurityOrderById();

    @Query("""
    SELECT sec FROM Security sec
    WHERE sec.currencies.id = :currencyId
""")
    List<Security> getSecurityByCurrencyId(
            @Param("currencyId") Long currencyId
    );

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

    /**
     * Returns paginated securities filtered by:
     * <ul>
     *   <li>currency ID</li>
     *   <li>note status (e.g. NEW, USED)</li>
     *   <li>optional visibility flag</li>
     * </ul>
     *
     * <p>If {@code visible} is {@code null}, notes are returned regardless of visibility.</p>
     *
     * <p>Results are ordered by note denomination in ascending order.</p>
     *
     * @param currencyId required currency identifier
     * @param status     required note status
     * @param visible    optional visibility filter (true / false / null)
     * @param pageable   pagination and sorting information
     * @return paginated list of {@link Security}
     */
    @Query("""
    SELECT sec
    FROM Security sec
        JOIN sec.statuses stat
    WHERE sec.currencies.id = :currencyId
      AND stat.status = :status
      AND (:visible IS NULL OR sec.visible = :visible)
    ORDER BY sec.denomination
""")
    Page<Security> securityPageable(
            @Param("currencyId") Long currencyId,
            @Param("status") String status,
            @Param("visible") Boolean visible,
            Pageable pageable
    );

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