package pecunia_22.models.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Medal;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;

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

    /**
     * Zwraca listę krajów wraz z liczbą not
     * o określonym statusie.
     * ------
     * Zapytanie wykorzystywane do statystyk / zestawień (np. dashboard).
     *
     * @param status     status noty (np. COLLECTION, FOR_SALE, SOLD)
     * @param visible    opcjonalny filtr widoczności:
     *                   - null  → wszystkie rekordy
     *                   - true  → tylko widoczne
     *                   - false → tylko niewidoczne
     * -------
     * @return lista krajów posortowana alfabetycznie według nazwy kraju (EN),
     *         zawierająca liczbę not dla każdego kraju
     */
    @Query("""
    SELECT new pecunia_22.models.sqlClass.CountryByStatus(
        con.continentEn,
        con.continentCode,
        cou.id,
        cou.countryEn,
        cou.countryPl,
        COUNT(medal.id)
    )
    FROM Medal medal
            JOIN medal.statuses stat
            JOIN medal.currencies cur
            JOIN cur.countries cou
            JOIN cou.continents con
    WHERE stat.status = :status
      AND (:visible IS NULL OR medal.visible = :visible)
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
     * Zwraca statystyki medali pogrupowane według waluty
     * dla zadanego statusu i kraju.
     *
     * Zapytanie:
     *  - filtruje medale po wymaganym statusie,
     *  - ogranicza wyniki do wybranego kraju,
     *  - opcjonalnie filtruje po widoczności.
     *
     * Jeśli parametr {@code visible} ma wartość {@code null},
     * zwracane są wszystkie medale (widok administratora).
     *
     * Wyniki są grupowane według waluty w obrębie danego kraju
     * i sortowane rosnąco po serii waluty.
     *
     * @param status wymagany status medalu (np. KOLEKCJA, NA_SPRZEDAZ)
     * @param countryId identyfikator kraju
     * @param visible opcjonalny filtr widoczności (true / false / null)
     * @return lista statystyk walut z liczbą medali
     */
    @Query("""
    SELECT new pecunia_22.models.sqlClass.CurrencyByStatus(
        cou.id,
        con.continentEn,
        cou.countryEn,
        cou.countryPl,
        cur.id,
        cur.currencySeries,
        COUNT(medal.id)
    )
    FROM Medal medal
            JOIN medal.statuses stat
            JOIN medal.currencies cur
            JOIN cur.countries cou
            JOIN cou.continents con
    WHERE stat.status = :status
      AND cou.id = :countryId
      AND (:visible IS NULL OR medal.visible = :visible)
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
