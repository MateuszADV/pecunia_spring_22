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
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.sql.Date;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    /**
     * Pobiera wszystkie banknoty (Note) – widoczne dla administratora.
     * -------
     * Umożliwia opcjonalne filtrowanie po walucie.
     * Jeśli `currencyId` jest null, zwraca wszystkie banknoty niezależnie od waluty.
     *
     * @param currencyId  ID waluty, po której można filtrować (opcjonalnie)
     * @return Lista encji Note z pełnymi danymi banknotów
     */
    // 1️⃣ Admin – pełna encja, z opcjonalnym filtrem po walucie
    @Query("SELECT note FROM Note note " +
           "WHERE (:currencyId IS NULL OR note.currencies.id = :currencyId)")
    List<Note> getNoteByCurrencyId(@Param("currencyId") Long currencyId);

    @Query("SELECT n FROM Note n")
    Page<Note> findFirstForUpdate(Pageable pageable);


    /**
     * Pobiera wszystkie banknoty (Note) widoczne dla użytkownika.
     * --------
     * Filtrowanie odbywa się po walucie (`currencyId`) oraz widoczności (`visible`).
     *
     * @param currencyId  ID waluty, po której filtrujemy
     * @param visible     Widoczność banknotu dla użytkownika (true = widoczne)
     * @return Lista encji Note spełniających kryteria filtrowania
     */
    @Query(value = "SELECT note FROM Note note " +
            "WHERE note.currencies.id = ?1 AND note.visible = ?2")
    List<Note> getNoteByCurrencyId(Long currencyId, Boolean visible);

    /**
     * Zwraca listę krajów pogrupowanych według kontynentu dla podanego statusu not.
     * ------
     * Zapytanie agregujące (GROUP BY), wykorzystywane do statystyk oraz widoków zbiorczych
     * (np. listy krajów z liczbą not w danym statusie).
     * ------
     *   Każdy rekord reprezentuje jeden kraj
     *   Wartość {@code total} oznacza liczbę not przypisanych do kraju
     *   Wynik zwracany jest jako projekcja DTO {@link CountryByStatus}
     *
     * @param status status not (np. COLLECTION, SELL, SOLD)
     * @return lista krajów z liczbą not w danym statusie
     */
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
        JOIN note.statuses stat
        JOIN note.currencies cur
        JOIN cur.countries cou
        JOIN cou.continents con
    WHERE stat.status = :status
    GROUP BY
        con.continentEn,
        con.continentCode,
        cou.id,
        cou.countryEn,
        cou.countryPl
    ORDER BY cou.countryEn
""")
    List<CountryByStatus> countryByStatus(
            @Param("status") String status
    );


    /**
     * Zwraca listę krajów w obrębie danego kontynentu wraz z liczbą not
     * o określonym statusie.
     * ------
     * Zapytanie wykorzystywane do statystyk / zestawień (np. dashboard).
     *
     * @param status     status noty (np. COLLECTION, FOR_SALE, SOLD)
     * @param continent  nazwa kontynentu (np. "Europe", "Asia")
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

    /**
     * Zwraca statystyki walut dla danego kraju i statusu notatek.
     *
     * @param status     status noty (np. COLLECTION, FOR_SALE)
     * @param countryId  identyfikator kraju
     * @param visible    opcjonalny filtr widoczności:
     *                   - true  → tylko widoczne
     *                   - false → tylko niewidoczne
     *                   - null  → bez filtra widoczności
     *
     * @return lista statystyk walut (ilość not według serii waluty)
     */
    @Query("""
    SELECT new pecunia_22.models.sqlClass.CurrencyByStatus(
        cou.id,
        con.continentEn,
        cou.countryEn,
        cou.countryPl,
        cur.id,
        cur.currencySeries,
        COUNT(cur.id)
    )
    FROM Note note
        JOIN note.statuses stat
        JOIN note.currencies cur
        JOIN cur.countries cou
        JOIN cou.continents con
    WHERE stat.status = :status
      AND cou.id = :countryId
      AND (:visible IS NULL OR note.visible = :visible)
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
     * Pobiera listę banknotów (Note) z uwzględnieniem statusu i miejsca zakupu (bought).
     * ----
     * Każdy rekord jest mapowany do klasy GetNotesByStatus, która zawiera:
     * - dane kraju i kontynentu (countryId, countryEn, countryPl, continent)
     * - dane waluty (currencyId, currencySeries)
     * - informacje o zakupie (bought)
     * - szczegóły banknotu (denomination, nameCurrency, itemDate, priceBuy, priceSell, quantity, unitQuantity)
     * - encje powiązane: Quality i Making
     * - dodatkowe informacje (visible, description, aversPath, reversePath, noteId, width, height)
     * ----
     * @param status  Status banknotu (np. "kolekcja", "na sprzedaż")
     * @param bought  Nazwa miejsca zakupu banknotu
     * @return Lista obiektów GetNotesByStatus z wszystkimi powiązanymi informacjami
     */
    @Query("""
SELECT new map(
    cou.id AS countryId,
    cou.countryEn AS countryEn,
    cou.countryPl AS countryPl,
    cur.id AS currencyId,
    cur.currencySeries AS currencySeries,
    bou.name AS bought,
    note.denomination AS denomination,
    note.nameCurrency AS nameCurrency,
    note.itemDate AS itemDate,
    note.priceBuy AS priceBuy,
    note.priceSell AS priceSell,
    note.quantity AS quantity,
    note.unitQuantity AS unitQuantity,
    note.qualities AS qualities,
    note.visible AS visible,
    note.description AS description,
    note.aversPath AS aversPath,
    note.reversePath AS reversePath,
    note.id AS noteId,
    note.makings AS makings,
    note.width AS width,
    note.height AS height
)
    FROM Note note
        JOIN note.statuses stat
        JOIN note.boughts bou
        JOIN note.currencies cur
        JOIN cur.countries cou
    WHERE stat.status = :status
      AND bou.name = :bought
    ORDER BY cou.countryEn, note.denomination
""")
    List<Object[]> getNotesByStatusAndBought(
            @Param("status") String status,
            @Param("bought") String bought
    );

    /*
     * Pobiera listę notatek dla podanego statusu.
     * Każdy element Object[] zawiera w kolejności:
     * 0 - countryId (Long)
     * 1 - countryEn (String)
     * 2 - countryPl (String)
     * 3 - currencyId (Long)
     * 4 - currencySeries (String)
     * 5 - bought (String)
     * 6 - denomination (Double)
     * 7 - nameCurrency (String)
     * 8 - itemDate (String)
     * 9 - priceBuy (Double)
     * 10 - priceSell (Double)
     * 11 - quantity (Integer)
     * 12 - unitQuantity (String)
     * 13 - qualities (Quality entity)
     * 14 - visible (Boolean)
     * 15 - description (String)
     * 16 - aversPath (String)
     * 17 - reversePath (String)
     * 18 - noteId (Long)
     * 19 - makings (Making entity)
     * 20 - width (Integer)
     * 21 - height (Integer)
     */

    /**
     * Zwraca listę not dla danego statusu.
     * ----------
     * Zapytanie wykorzystywane do wyświetlania list not (widoki admina),
     * z możliwością opcjonalnego:
     *   wykluczenia statusu sprzedaży
     *   filtrowania po kraju
     * ------
     * Wynik zwracany jest jako {@code Object[]} i mapowany dalej do DTO
     * za pomocą ModelMappera.
     *
     * @param status wymagany status not (np. COLLECTION)
     * @param excludedStatusSell opcjonalny status sprzedaży do wykluczenia (NULL = bez filtra)
     * @param countryId opcjonalny identyfikator kraju (NULL = wszystkie kraje)
     * @return lista not jako Object[]
     */
    @Query("""
SELECT new map(
    cou.id AS countryId,
    cou.countryEn AS countryEn,
    cou.countryPl AS countryPl,
    cur.id AS currencyId,
    cur.currencySeries AS currencySeries,
    bou.name AS bought,
    note.denomination AS denomination,
    note.nameCurrency AS nameCurrency,
    note.itemDate AS itemDate,
    note.priceBuy AS priceBuy,
    note.priceSell AS priceSell,
    note.quantity AS quantity,
    note.unitQuantity AS unitQuantity,
    note.qualities AS qualities,
    note.visible AS visible,
    note.description AS description,
    note.aversPath AS aversPath,
    note.reversePath AS reversePath,
    note.id AS noteId,
    note.makings AS makings,
    note.width AS width,
    note.height AS height
)
FROM Note note
        JOIN note.statuses stat
        JOIN note.boughts bou
        JOIN note.currencies cur
        JOIN cur.countries cou
    WHERE stat.status = :status
      AND (:excludedStatusSell IS NULL OR note.statusSell <> :excludedStatusSell)
      AND (:countryId IS NULL OR cou.id = :countryId)
    ORDER BY cou.countryEn, note.denomination
""")
    List<Object[]> getNotesByStatus(
            @Param("status") String status,
            @Param("excludedStatusSell") String excludedStatusSell,
            @Param("countryId") Long countryId
    );


    /**
     * Returns paginated notes filtered by:
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
     * @return paginated list of {@link Note}
     */
    @Query("""
    SELECT note
    FROM Note note
        JOIN note.statuses stat
    WHERE note.currencies.id = :currencyId
      AND stat.status = :status
      AND (:visible IS NULL OR note.visible = :visible)
    ORDER BY note.denomination
""")
    Page<Note> notePageable(
            @Param("currencyId") Long currencyId,
            @Param("status") String status,
            @Param("visible") Boolean visible,
            Pageable pageable
    );


//
//    @Query(value = "SELECT note FROM Note note " +
//            "  LEFT JOIN Status stat " +
//            "    ON stat.status = ?2 " +
//            "WHERE note.currencies.id = ?1 AND stat = note.statuses " +
//            "ORDER BY note.denomination")
//    Page<Note> notePageable(Long currencyId, String status, final Pageable pageable);
//
//    @Query(value = "SELECT note FROM Note note " +
//            "  LEFT JOIN Status stat " +
//            "    ON stat.status = ?2 " +
//            "WHERE note.currencies.id = ?1 AND stat = note.statuses  AND note.visible = ?3 " +
//            "ORDER BY note.denomination")
//    Page<Note> notePageable(Long currencyId, String status, Boolean visible, final Pageable pageable);

//    *******************
//    ****NOTE UPDATE****
//    *******************


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
        UPDATE Note n
        SET
            n.currencies.id = :#{#note.currencies.id},
            n.denomination = :#{#note.denomination},
            n.dateBuy = :#{#note.dateBuy},
            n.nameCurrency = :#{#note.nameCurrency},
            n.series = :#{#note.series},
            n.boughts.id = :#{#note.boughts.id},
            n.itemDate = :#{#note.itemDate},
            n.quantity = :#{#note.quantity},
            n.unitQuantity = :#{#note.unitQuantity},
            n.actives.id = :#{#note.actives.id},
            n.priceBuy = :#{#note.priceBuy},
            n.priceSell = :#{#note.priceSell},
            n.makings.id = :#{#note.makings.id},
            n.qualities.id = :#{#note.qualities.id},
            n.width = :#{#note.width},
            n.height = :#{#note.height},
            n.statuses.id = :#{#note.statuses.id},
            n.imageTypes.id = :#{#note.imageTypes.id},
            n.statusSell = :#{#note.statusSell},
            n.visible = :#{#note.visible},
            n.description = :#{#note.description},
            n.aversPath = :#{#note.aversPath},
            n.reversePath = :#{#note.reversePath},
            n.serialNumber = :#{#note.serialNumber}
        WHERE n.id = :#{#note.id}
    """)
    void updateNote(@Param("note") Note note);

//    @Modifying
//    @Transactional
//    @Query("""
//UPDATE Note note SET
//    note.currencies.id = :currencyId,
//    note.denomination = :denomination,
//    note.dateBuy = :dateBuy,
//    note.nameCurrency = :nameCurrency,
//    note.series = :series,
//    note.boughts.id = :boughtId,
//    note.itemDate = :itemDate,
//    note.quantity = :quantity,
//    note.unitQuantity = :unitQuantity,
//    note.actives.id = :activesId,
//    note.priceBuy = :priceBuy,
//    note.priceSell = :priceSell,
//    note.makings.id = :makingId,
//    note.qualities.id = :qualityId,
//    note.width = :width,
//    note.height = :height,
//    note.statuses.id = :statusId,
//    note.imageTypes.id = :imageTypeId,
//    note.statusSell = :statusSell,
//    note.visible = :visible,
//    note.description = :description,
//    note.aversPath = :aversPath,
//    note.reversePath = :reversePath,
//    note.serialNumber = :serialNumber
//WHERE note.id = :noteId
//""")
//    void updateNote(
//            @Param("currencyId") Long currencyId,
//            @Param("denomination") Double denomination,
//            @Param("dateBuy") Date dateBuy,
//            @Param("nameCurrency") String nameCurrency,
//            @Param("series") String series,
//            @Param("boughtId") Long boughtId,
//            @Param("itemDate") String itemDate,
//            @Param("quantity") Integer quantity,
//            @Param("unitQuantity") String unitQuantity,
//            @Param("activesId") Long activesId,
//            @Param("priceBuy") Double priceBuy,
//            @Param("priceSell") Double priceSell,
//            @Param("makingId") Long makingId,
//            @Param("qualityId") Long qualityId,
//            @Param("width") Integer width,
//            @Param("height") Integer height,
//            @Param("statusId") Long statusId,
//            @Param("imageTypeId") Long imageTypeId,
//            @Param("statusSell") String statusSell,
//            @Param("visible") Boolean visible,
//            @Param("description") String description,
//            @Param("aversPath") String aversPath,
//            @Param("reversePath") String reversePath,
//            @Param("serialNumber") String serialNumber,
//            @Param("noteId") Long noteId
//    );
}
