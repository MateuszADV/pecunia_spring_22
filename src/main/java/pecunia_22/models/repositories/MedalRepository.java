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

import java.util.List;
import java.util.Optional;

@Repository
public interface MedalRepository extends JpaRepository<Medal, Long> {
    @Query(value = "SELECT medal FROM Medal medal " +
            " WHERE medal.currencies.id = ?1")
    List<Medal> getMedalByCurrencyId(Long currencyId);

    @Query(value = "SELECT medal FROM Medal medal " +
            " WHERE medal.currencies.id = ?1 AND medal.visible = ?2")
    List<Medal> getMedalByCurrencyId(Long currencyId, Boolean visible);

    Optional<Medal> findByIdAndVisibleTrue(Long id);



    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
    UPDATE Medal m
    SET
        m.currencies.id = :#{#medal.currencies.id},
        m.denomination  = :#{#medal.denomination},
        m.dateBuy       = :#{#medal.dateBuy},
        m.nameCurrency  = :#{#medal.nameCurrency},
        m.series        = :#{#medal.series},

        m.boughts.id    = :#{#medal.boughts.id},
        m.itemDate      = :#{#medal.itemDate},
        m.quantity      = :#{#medal.quantity},
        m.unitQuantity  = :#{#medal.unitQuantity},
        m.actives.id    = :#{#medal.actives.id},
        m.priceBuy      = :#{#medal.priceBuy},
        m.priceSell     = :#{#medal.priceSell},

        m.qualities.id  = :#{#medal.qualities.id},
        m.diameter      = :#{#medal.diameter},
        m.thickness     = :#{#medal.thickness},
        m.weight        = :#{#medal.weight},
        m.statuses.id   = :#{#medal.statuses.id},
        m.imageTypes.id = :#{#medal.imageTypes.id},

        m.statusSell    = :#{#medal.statusSell},
        m.visible       = :#{#medal.visible},
        m.composition   = :#{#medal.composition},
        m.description   = :#{#medal.description},
        m.aversPath     = :#{#medal.aversPath},
        m.reversePath   = :#{#medal.reversePath}
    WHERE m.id = :#{#medal.id}
""")
    void updateMedal(@Param("medal") Medal medal);

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

    @Query("""
    SELECT medal
    FROM Medal medal
        JOIN medal.statuses stat
    WHERE medal.currencies.id = :currencyId
      AND stat.status = :status
      AND (:visible IS NULL OR medal.visible = :visible)
    ORDER BY medal.denomination
""")
    Page<Medal> medalPageable(
            @Param("currencyId") Long currencyId,
            @Param("status") String status,
            @Param("visible") Boolean visible,
            Pageable pageable
    );
}