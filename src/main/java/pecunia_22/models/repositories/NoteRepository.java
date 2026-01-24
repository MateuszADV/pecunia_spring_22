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
import pecunia_22.models.sqlClass.CountryByStatus;

import java.sql.Date;
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
//    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
//            "  FROM Note note" +
//            "  LEFT JOIN Status stat" +
//            "    ON stat.status = ?1" +
//            "  LEFT JOIN Currency cur" +
//            "    ON cur = note.currencies" +
//            "  LEFT JOIN Country cou" +
//            "    ON cou = cur.countries" +
//            "  LEFT JOIN Continent con" +
//            "    ON con = cou.continents" +
//            " WHERE stat = note.statuses AND con.continentEn = ?2" +
//            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
//            " ORDER BY cou.countryEn")
//    List<Object[]> countryByStatus(String status, String continent);


//    @Query("""
//    SELECT new pecunia_22.models.sqlClass.CountryByStatus(
//        con.continentEn,
//        con.continentCode,
//        cou.id,
//        cou.countryEn,
//        cou.countryPl,
//        COUNT(note.id)
//    )
//    FROM Note note
//    LEFT JOIN note.statuses stat
//    LEFT JOIN note.currencies cur
//    LEFT JOIN cur.countries cou
//    LEFT JOIN cou.continents con
//    WHERE stat.status = :status
//      AND con.continentEn = :continent
//    GROUP BY
//        con.continentEn,
//        con.continentCode,
//        cou.id,
//        cou.countryEn,
//        cou.countryPl
//    ORDER BY cou.countryEn
//""")
//    List<CountryByStatus> countryByStatus(
//            @Param("status") String status,
//            @Param("continent") String continent
//    );

    @Query("""
    SELECT new pecunia_22.models.sqlClass.CountryByStatus(
        con.continentEn,
        con.continentCode,
        cou.id,
        cou.countryEn,
        cou.countryPl,
        COUNT(note.id)
    )
    FROM Note note
    LEFT JOIN note.statuses stat
    LEFT JOIN note.currencies cur
    LEFT JOIN cur.countries cou
    LEFT JOIN cou.continents con
    WHERE stat.status = :status
      AND con.continentEn = :continent
      AND (:visible IS NULL OR note.visible = :visible)
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
            @Param("continent") String continent,
            @Param("visible") Boolean visible
    );




//    @Query(value = "SELECT new map(con.continentEn AS continent, con.continentCode AS continentCode, cou.id AS countryId, cou.countryEn AS countryEn, cou.countryPl AS countryPl, COUNT(cou.countryEn) AS total) " +
//            "  FROM Note note" +
//            "  LEFT JOIN Status stat" +
//            "    ON stat.status = ?1" +
//            "  LEFT JOIN Currency cur" +
//            "    ON cur = note.currencies" +
//            "  LEFT JOIN Country cou" +
//            "    ON cou = cur.countries" +
//            "  LEFT JOIN Continent con" +
//            "    ON con = cou.continents" +
//            " WHERE stat = note.statuses AND con.continentEn = ?2 AND note.visible = ?3" +
//            " GROUP BY cou.countryEn, cou.countryPl, cou.id, con.continentEn, con.continentCode" +
//            " ORDER BY cou.countryEn")
//    List<Object[]> countryByStatus(String status, String continent, Boolean visible);

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
            "    ON stat.status = ?2 " +
            "WHERE note.currencies.id = ?1 AND stat = note.statuses  AND note.visible = ?3 " +
            "ORDER BY note.denomination")
    Page<Note> notePageable(Long currencyId, String status, Boolean visible, final Pageable pageable);

//    *******************
//    ****NOTE UPDATE****
//    *******************

    @Transactional
    @Modifying
    @Query(value = "update Note note set note.currencies.id = ?1, note.denomination = ?2, note.dateBuy = ?3, note.nameCurrency = ?4, note.series = ?5, " +
                   "note.boughts.id = ?6, note.itemDate = ?7, note.quantity = ?8, note.unitQuantity = ?9, note.actives.id = ?10, note.priceBuy = ?11, note.priceSell = ?12, " +
                   "note.makings.id = ?13, note.qualities.id = ?14, note.width = ?15, note.height = ?16, note.statuses.id = ?17, note.imageTypes.id = ?18, " +
                   "note.statusSell = ?19, note.visible = ?20, note.description = ?21, note.aversPath = ?22, note.reversePath = ?23, note.serialNumber = ?24 "+
                   "where note.id = ?25")
    void updateNote(Long currencyId, Double denomination, Date dateBuy, String nameCurrency, String series,
                    Long boughtsId, String itemDate, Integer quantity, String unitQuantity, Long activesId, Double priceBuy, Double priceSell,
                    Long making, Long quality, Integer width, Integer height, Long status, Long imageType,
                    String statusSell, Boolean visible, String  description, String aversePath, String ReversePath, String serialNumber,
                    Long noteId);
}
