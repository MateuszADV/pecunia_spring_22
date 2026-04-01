package pecunia_22.models.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Currency;
import pecunia_22.models.dto.currency.CurrencyDtoWithCount;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query("""
        SELECT cur FROM Currency cur
        JOIN cur.countries c
        JOIN cur.patterns p
        WHERE c.id = :countryId
        AND p.pattern = :pattern
        ORDER BY cur.currencySeries ASC
    """)
    List<Currency> getCurrencyByCountryByPattern(
            @Param("countryId") Long countryId,
            @Param("pattern") String pattern
    );


//    @Query(value = "SELECT cur FROM Currency cur " +
//            "WHERE cur.countries.id = ?1 " +
//            "AND cur.patterns.pattern = ?2 " +
//            "ORDER BY cur.currencySeries ASC ")
//    List<Currency> getCurrencyByCountryByPattern(Long countryId, String pattern);



    @Query("""
        SELECT cur FROM Currency cur
        JOIN cur.countries c
        JOIN cur.patterns p
        WHERE c.id = :countryId
        AND p.pattern = :pattern
        ORDER BY cur.currencySeries ASC
    """)
    List<Currency> findByCountryAndPattern(
            @Param("countryId") Long countryId,
            @Param("pattern") String pattern
    );

    /*
    WERSJA POPRAWIONA
     */


    @Query("""
    SELECT cur FROM Currency cur
    JOIN cur.countries c
    JOIN cur.patterns p
    WHERE c.countryEn = :countryEn
      AND p.pattern = :pattern
    ORDER BY cur.currencySeries ASC
""")
    List<Currency> getCurrencyByCountryByPattern(
            @Param("countryEn") String countryEn,
            @Param("pattern") String pattern
    );


    @Query("""
    SELECT cur FROM Currency cur
    JOIN cur.countries c
    JOIN cur.patterns p
    WHERE c.countryEn = :countryEn
      AND p.id = :patternId
    ORDER BY cur.currencySeries ASC
""")
    List<Currency> getCurrencyByCountryByPatternId(
            @Param("countryEn") String countryEn,
            @Param("patternId") Long patternId
    );


    /** Pobiera waluty dla kraju i pattern wraz z liczbą powiązanych elementów (MEDAL/COIN/NOTE/SECURITY);
     * czerwone podkreślenie w IDE wynika z ostrzeżeń JPQL przy JOIN z warunkiem ON. */
    @Query("""
        SELECT new pecunia_22.models.dto.currency.CurrencyDtoWithCount(
            cur.id,
            cur.currencySeries,
            pat.pattern,
            cur.countries.countryEn,
            CASE 
                WHEN pat.pattern = 'MEDAL' THEN 
                    (SELECT COUNT(m) FROM Medal m WHERE m.currencies.id = cur.id)
                WHEN pat.pattern = 'COIN' THEN 
                    (SELECT COUNT(c) FROM Coin c WHERE c.currencies.id = cur.id)
                WHEN pat.pattern = 'NOTE' THEN 
                    (SELECT COUNT(n) FROM Note n WHERE n.currencies.id = cur.id)
                WHEN pat.pattern = 'SECURITY' THEN 
                    (SELECT COUNT(s) FROM Security s WHERE s.currencies.id = cur.id)
                ELSE 0L
            END
        )
        FROM Currency cur
        JOIN cur.patterns pat
        WHERE cur.countries.id = :countryId
        AND pat.pattern = :pattern
        ORDER BY cur.currencySeries ASC
    """)
    List<CurrencyDtoWithCount> getCurrencyWithDynamicCount(@Param("countryId") Long countryId,
                                                           @Param("pattern") String pattern);



//    @Query(value = "SELECT cur FROM Currency cur " +
//            "  LEFT JOIN Country cou " +
//            "    ON cou.countryEn = ?1 " +
//            "  LEFT JOIN Pattern pat " +
//            "    ON pat.pattern = ?2 " +
//            " WHERE cur.countries.id = cou.id AND cur.patterns.id = pat.id")
//    List<Currency> getCurrencyByCountryByPattern(String countryEn, String pattern);

    //    ***********************
    //    ****CURRENCY UPDATE****
    //    ***********************

//    @Transactional
//    @Modifying
//    @Query(value = "update Currency cur set cur.cod = ?1, cur.patterns.id = ?2" +
//                   "where cur.id = ?25")
//    void updateCurrency( String cod, Long pattern, Long coinId);
}
