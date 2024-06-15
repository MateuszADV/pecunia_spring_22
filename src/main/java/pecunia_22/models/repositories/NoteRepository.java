package pecunia_22.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Note;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query(value = "SELECT note FROM Note note " +
            "WHERE note.currencies.id = ?1")
    List<Note> getNoteByCurrencyId(Long currencyId);

    @Query(value = "SELECT note FROM Note note " +
            "WHERE note.currencies.id = ?1 AND note.visible = ?2")
    List<Note> getNoteByCurrencyId(Long currencyId, Boolean visible);


    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = note.statuses" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status);

    /**
     * @param status
     * @param continent
     * @return
     */
    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = note.statuses AND con.continentEn = ?2" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status, String continent);

    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = note.statuses AND con.continentEn = ?2 AND note.visible = ?3" +
            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
            " ORDER BY cou.countryEn")
    List<Object[]> countryByStatus(String status, String continent, Boolean visible);

    @Query(value = "SELECT new map(cou.id AS countryId, con.continentEn AS continentEn, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, COUNT(cur.currencySeries) AS total) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = note.statuses AND cou.id = ?2" +
            " GROUP BY cou.countryEn, con.continentEn, cou.countryPl, cou.id, cur.currencySeries, cur.id" +
            " ORDER BY cur.currencySeries")
    List<Object[]> currencyByStatus(String status, Long countryId);

    @Query(value = "SELECT new map(cou.id AS countryId, con.continentEn AS continentEn, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, COUNT(cur.currencySeries) AS total) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Continent con" +
            "    ON con = cou.continents" +
            " WHERE stat = note.statuses AND cou.id = ?2 AND note.visible = ?3" +
            " GROUP BY cou.countryEn, con.continentEn, cou.countryPl, cou.id, cur.currencySeries, cur.id" +
            " ORDER BY cur.currencySeries")
    List<Object[]> currencyByStatus(String status, Long countryId, Boolean visible);

    @Query(value = "SELECT new map(note.qualities AS qualities, note.id AS noteId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, bou.name AS bought, note.denomination AS denomination, note.nameCurrency AS nameCurrency, note.itemDate AS itemDate, " +
            "note.priceBuy AS priceBuy, note.priceSell AS priceSell, note.quantity AS quantity, note.unitQuantity AS unitQuantity, " +
            "note.width AS width, note.height AS height, note.visible AS visible, note.description AS description, " +
            "note.aversPath AS aversPath, note.reversePath AS reversePath ) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Bought bou" +
            "    ON bou = note.boughts" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = note.statuses AND bou.name = ?2" +
            " GROUP BY note.qualities, note.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, bou.name, note.denomination, note.nameCurrency, note.itemDate, " +
            "          note.priceBuy, note.priceSell, note.quantity, note.unitQuantity, note.width, note.height, note.visible, note.description, note.aversPath, note.reversePath " +
            " ORDER BY cou.countryEn, note.denomination")
    List<Object[]> getNotesByStatusAndBought(String status, String bought);

    @Query(value = "SELECT new map(note.qualities AS qualities, note.makings AS makings, note.id AS noteId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, cur.patterns AS patterns, bou.name AS bought, note.denomination AS denomination, note.nameCurrency AS nameCurrency, note.itemDate AS itemDate, " +
            "note.priceBuy AS priceBuy, note.priceSell AS priceSell, note.quantity AS quantity, note.unitQuantity AS unitQuantity, " +
            "note.width AS width, note.height AS height, note.visible AS visible, note.description AS description, " +
            "note.aversPath AS aversPath, note.reversePath AS reversePath ) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Bought bou" +
            "    ON bou = note.boughts" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = note.statuses AND note.statusSell != ?2" +
            " GROUP BY note.qualities, note.makings, note.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, cur.patterns, bou.name, note.denomination, note.nameCurrency, note.itemDate, " +
            "          note.priceBuy, note.priceSell, note.quantity, note.unitQuantity, note.width, note.height, note.visible, note.description, note.aversPath, note.reversePath " +
            " ORDER BY cou.countryEn, note.denomination")
    List<Object[]> getNotesByStatus(String status, String statusSell);

    @Query(value = "SELECT new map(note.qualities AS qualities, note.makings AS makings, note.id AS noteId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, cur.patterns AS patterns, bou.name AS bought, note.denomination AS denomination, note.nameCurrency AS nameCurrency, note.itemDate AS itemDate, " +
            "note.priceBuy AS priceBuy, note.priceSell AS priceSell, note.quantity AS quantity, note.unitQuantity AS unitQuantity, " +
            "note.width AS width, note.height AS height, note.visible AS visible, note.description AS description, " +
            "note.aversPath AS aversPath, note.reversePath AS reversePath ) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Bought bou" +
            "    ON bou = note.boughts" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            " WHERE stat = note.statuses" +
            " GROUP BY note.qualities, note.makings, note.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, cur.patterns, bou.name, note.denomination, note.nameCurrency, note.itemDate, " +
            "          note.priceBuy, note.priceSell, note.quantity, note.unitQuantity, note.width, note.height, note.visible, note.description, note.aversPath, note.reversePath " +
            " ORDER BY cou.countryEn, note.denomination")
    List<Object[]> getNotesByStatus(String status);

    @Query(value = "SELECT new map(note.qualities AS qualities, note.id AS noteId, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, cur.id AS currencyId, " +
            "cur.currencySeries AS currencySeries, bou.name AS bought, note.denomination AS denomination, note.nameCurrency AS nameCurrency, note.itemDate AS itemDate, " +
            "note.priceBuy AS priceBuy, note.priceSell AS priceSell, note.quantity AS quantity, note.unitQuantity AS unitQuantity, " +
            "note.width AS width, note.height AS height, note.visible AS visible, note.description AS description, " +
            "note.aversPath AS aversPath, note.reversePath AS reversePath ) " +
            "  FROM Note note" +
            "  LEFT JOIN Status stat" +
            "    ON stat.status = ?1" +
            "  LEFT JOIN Bought bou" +
            "    ON bou = note.boughts" +
            "  LEFT JOIN Currency cur" +
            "    ON cur = note.currencies" +
            "  LEFT JOIN Country cou" +
            "    ON cou = cur.countries" +
            "  LEFT JOIN Quality qua" +
            "    ON qua = note.qualities" +
            " WHERE stat = note.statuses AND cou.id = ?2" +
            " GROUP BY note.qualities, note.id, cou.id, cou.countryEn, cou.countryPl, cur.id, cur.currencySeries, bou.name, note.denomination, note.nameCurrency, note.itemDate, " +
            "          note.priceBuy, note.priceSell, note.quantity, note.unitQuantity, note.width, note.height, note.visible, note.description, note.aversPath, note.reversePath " +
            " ORDER BY cou.countryEn, note.denomination")
    List<Object[]> getNotesByStatus(String status, Long countrtyId);

    @Query(value = "SELECT note FROM Note note " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2 " +
            "WHERE note.currencies.id = ?1 AND stat = note.statuses " +
            "ORDER BY note.denomination")
    Page<Note> notePageable(Long currencyId, String status, final Pageable pageable);

    @Query(value = "SELECT note FROM Note note " +
            "  LEFT JOIN Status stat " +
            "    ON stat.status = ?2" +
            "WHERE note.currencies.id = ?1 AND stat = note.statuses  AND note.visible = ?3 " +
            "ORDER BY note.denomination")
    Page<Note> notePageable(Long currencyId, String status, Boolean visible, final Pageable pageable);
}
