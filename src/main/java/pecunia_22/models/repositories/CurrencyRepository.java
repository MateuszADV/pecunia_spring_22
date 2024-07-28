package pecunia_22.models.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Currency;

import java.sql.Date;
import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    @Query(value = "SELECT cur FROM Currency cur " +
            "WHERE cur.countries.id = ?1 " +
            "AND cur.pattern = ?2 " +
            "ORDER BY cur.currencySeries ASC ")
    List<Currency> getCurrencyByCountryByPattern(Long countryId, String pattern);

    /*
    WERSJA POPRAWIONA
     */

    @Query(value = "SELECT cur FROM Currency cur " +
            "  LEFT JOIN Country cou " +
            "    ON cou.countryEn = ?1 " +
            "  LEFT JOIN Pattern pat " +
            "    ON pat.pattern = ?2 " +
            " WHERE cur.countries.id = cou.id AND cur.patterns.id = pat.id")
    List<Currency> getCurrencyByCountryByPattern(String countryEn, String pattern);

    //    ***********************
    //    ****CURRENCY UPDATE****
    //    ***********************

//    @Transactional
//    @Modifying
//    @Query(value = "update Currency cur set cur.cod = ?1, cur.patterns.id = ?2" +
//                   "where cur.id = ?25")
//    void updateCurrency( String cod, Long pattern, Long coinId);
}
