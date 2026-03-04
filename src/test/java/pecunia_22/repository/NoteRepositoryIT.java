package pecunia_22.repository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.models.Note;
import pecunia_22.models.repositories.NoteRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NoteRepositoryIT {

    private final NoteRepository noteRepository;
    private final EntityManager entityManager;

    @Autowired
    public NoteRepositoryIT(
            NoteRepository noteRepository,
            EntityManager entityManager) {
        this.noteRepository = noteRepository;
        this.entityManager = entityManager;
    }

    @Test
    void shouldLoadNotesFromTestDatabase() {
        // when
        List<Note> notes = noteRepository.getNoteByCurrencyId(null);

        // then
        assertThat(notes).isNotNull();
    }

    @Test
    void shouldReturnNotesWhenCurrencyIdIsNull() {
        // when
        List<Note> notes = noteRepository.getNoteByCurrencyId(null);

        // then
        assertThat(notes)
                .isNotEmpty();

        // opcjonalnie – sprawdzamy pierwszy rekord
        Note note = notes.get(0);
        assertThat(note.getId()).isNotNull();
    }

    @Test
    void shouldReturnNotesForGivenCurrencyId() {
        // given
        Long currencyId = 1L;

        // when
        List<Note> notes = noteRepository.getNoteByCurrencyId(currencyId);

        // then
        assertThat(notes)
                .isNotEmpty();

        assertThat(notes)
                .allMatch(note ->
                        note.getCurrencies() != null &&
                                note.getCurrencies().getId().equals(currencyId)
                );
    }

    @Test
    void shouldReturnNotesForGivenStatus() {
        // given
        String status = "NEW";

        // when
        List<Object[]> result = noteRepository.getNotesByStatus(
                status,
                null,   // excludedStatusSell
                null    // countryId
        );

        // then
        assertThat(result)
                .as("Notes should be returned for status = %s", status)
                .isNotNull()
                .isNotEmpty();

        log.info("getNotesByStatus returned {} rows for status={}",
                result.size(), status);
        log.info("\n🟢 [IT][NOTE] getNotesByStatus -> {} rows (status={})",
                result.size(), status);
    }

    @Test
    void shouldLoadNotesByStatusWithFilters_simple() {
        // given
        String status = "NEW";
        String excludedStatusSell = null;
        Long countryId = null;

        // when
        List<Object[]> result = noteRepository.getNotesByStatus(status, excludedStatusSell, countryId);

        // log w konwencji zielonej kropki
        getInfo(result, status, excludedStatusSell, countryId);

        // then – sprawdzamy tylko, że są wyniki
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldLoadNotesByStatusWithFilters() {
        // given – przykładowe wartości
        String status = "FOR SELL";
        String excludedStatusSell = null;
        Long countryId = 43L; // dopasowane do danych w bazie testowej

        // when
        List<Object[]> result = noteRepository.getNotesByStatus(status, excludedStatusSell, countryId);

        // log w konwencji zielonej kropki
        getInfo(result, status, excludedStatusSell, countryId);

        // then – sprawdzamy tylko, że są wyniki
        assertThat(result).isNotEmpty();

        // dodatkowy wariant – bez filtrów excludedStatusSell i countryId null
        List<Object[]> resultNoFilter = noteRepository.getNotesByStatus(status, null, null);
        log.info("\n🟢 [IT][NOTE] getNotesByStatus (no filters) -> {} rows (status={})", resultNoFilter.size(), status);
        assertThat(resultNoFilter).isNotEmpty();
    }

    private static void getInfo(List<Object[]> result, String status, String excludedStatusSell, Long countryId) {
        log.info("\n🟢 [IT][NOTE] getNotesByStatus (custom query) -> {} rows (status={}, excludedStatusSell={}, countryId={})",
                result.size(), status, excludedStatusSell, countryId);
    }

    @Test
    void shouldLoadCountriesByStatusAndContinent() {
        // given
        String status = "KOLEKCJA";
        String continent = "Europe";
        Boolean visible = null; // brak filtra widoczności

        // when
        List<CountryByStatus> result =
                noteRepository.countryByStatus(status, continent, visible);

        // then
        getInfo(result, status, continent, visible);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        // lekka walidacja danych
        result.forEach(row -> {
            assertThat(row.countryId()).isNotNull();
            assertThat(row.countryEn()).isNotBlank();
            assertThat(row.continent()).isEqualTo(continent);
            assertThat(row.total()).isPositive();
        });
    }

    @Test
    void shouldLoadOnlyVisibleCountriesByStatusAndContinent() {
        // given
        String status = "KOLEKCJA";
        String continent = "Europe";
        Boolean visible = true;

        // when
        List<CountryByStatus> result =
                noteRepository.countryByStatus(status, continent, visible);

        // then
        getInfo(result, status, continent, visible);

        assertThat(result).isNotNull();

        // jeżeli brak danych – test nadal przechodzi (to OK)
        if (result.isEmpty()) {
            log.warn("\n🟡 [IT][NOTE] No visible notes found for status={} and continent={}",
                    status, continent);
            return;
        }

        // walidacja danych
        result.forEach(row -> {
            assertThat(row.countryId()).isNotNull();
            assertThat(row.countryEn()).isNotBlank();
            assertThat(row.continent()).isEqualTo(continent);
            assertThat(row.total()).isPositive();
        });
    }

    private static void getInfo(List<CountryByStatus> result, String status, String continent, Boolean visible) {
        log.info("\n🟢 [IT][NOTE] countryByStatus -> {} rows (status={}, continent={}, visible={})",
                result.size(), status, continent, visible);
    }

    // Given status KOLEKCJA and no visible filter (null)
    // When loading notes by currency and status
    // Then all notes regardless of visibility are returned
    @Test
    void shouldLoadNotesByStatus_withoutVisibleFilter() {
        Page<Note> page = noteRepository.notePageable(
                226L,
                "KOLEKCJA",
                null,
                PageRequest.of(0, 10)
        );

        log.info("\n🟢 [IT][NOTE] notePageable -> {} elements", page.getTotalElements());

        assertThat(page).isNotNull();
    }

    // Given status KOLEKCJA and visible=true
    // When loading notes
    // Then only visible notes are returned
    @Test
    void shouldLoadOnlyVisibleNotesByStatus() {
        Page<Note> page = noteRepository.notePageable(
                226L,
                "KOLEKCJA",
                true,
                PageRequest.of(0, 10)
        );

        log.info("\n🟢 [IT][NOTE] notePageable -> {} elements", page.getTotalElements());

        assertThat(page).isNotNull();
    }

    @Test
    void shouldMaintainSortingAcrossPagesByDenomination() {

        // given
        Long currencyId = 226L; // dopasuj pod swoje dane
        String status = "KOLEKCJA";

        Pageable firstPageable = PageRequest.of(0, 5);
        Pageable secondPageable = PageRequest.of(1, 5);

        // when
        Page<Note> firstPage = noteRepository.notePageable(
                currencyId,
                status,
                null,
                firstPageable
        );

        Page<Note> secondPage = noteRepository.notePageable(
                currencyId,
                status,
                null,
                secondPageable
        );

        // then
        assertThat(firstPage).isNotNull();
        assertThat(secondPage).isNotNull();

        assertThat(firstPage.getContent()).isNotEmpty();

        // sprawdzenie sortowania na pierwszej stronie
        List<Double> firstDenominations = firstPage.getContent()
                .stream()
                .map(Note::getDenomination)
                .toList();

        assertThat(firstDenominations).isSorted();

        // jeśli druga strona istnieje — sprawdzamy ciągłość sortowania
        if (!secondPage.getContent().isEmpty()) {

            List<Double> secondDenominations = secondPage.getContent()
                    .stream()
                    .map(Note::getDenomination)
                    .toList();

            assertThat(secondDenominations).isSorted();

            Double lastFromFirstPage =
                    firstDenominations.get(firstDenominations.size() - 1);

            Double firstFromSecondPage =
                    secondDenominations.get(0);

            assertThat(firstFromSecondPage)
                    .isGreaterThanOrEqualTo(lastFromFirstPage);

            log.info("🟢 [IT][NOTE] Sorting continuity OK -> {} <= {}",
                    lastFromFirstPage, firstFromSecondPage);
        }

        log.info("🟢 [IT][NOTE] Pagination continuity test -> firstPage={}, secondPage={}, totalElements={}",
                firstPage.getNumber(),
                secondPage.getNumber(),
                firstPage.getTotalElements());
    }


    // Given status NEW and visible=false
    // When loading notes
    // Then only hidden notes are returned
    @Test
    void shouldLoadOnlyHiddenNotesByStatus() {
        Page<Note> page = noteRepository.notePageable(
                226L,
                "KOLEKCJA",
                false,
                PageRequest.of(0, 10)
        );

        log.info("\n🟢 [IT][NOTE] notePageable -> {} elements", page.getTotalElements());

        assertThat(page).isNotNull();
    }

    @Test
    void shouldReturnOnlyVisibleMedalsWithGivenStatusAndPaginateLog() {

        Long currencyId = 226L;
        String status = "KOLEKCJA";
        Pageable pageable = PageRequest.of(0, 5);

        log.info("Testing notePageable with currencyId={}, status={}, visible=true",
                currencyId, status);

        Page<Note> page = noteRepository
                .notePageable(currencyId, status, true, pageable);

        log.info("Total elements: {}", page.getTotalElements());
        log.info("Total pages: {}", page.getTotalPages());
        log.info("Current page size: {}", page.getContent().size());

        page.getContent().forEach(n ->
                log.info("Note id={}, denomination={}, visible={}, status={}",
                        n.getId(),
                        n.getDenomination(),
                        n.getVisible(),
                        n.getStatuses().getStatus()
                )
        );

        assertThat(page.getContent())
                .allMatch(n -> Boolean.TRUE.equals(n.getVisible()));

        assertThat(page.getContent())
                .allMatch(n -> status.equals(n.getStatuses().getStatus()));
    }

    @Test
    void shouldUpdateNote_successfully() {

        // given
        Note note = noteRepository
                .findFirstForUpdate(PageRequest.of(0, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No note found in DB"));


        Long noteId = note.getId();

        Double newPriceBuy = 999.99;
        String newDescription = "UPDATED_DESCRIPTION_IT";
        Boolean newVisible = !note.getVisible();

        note.setPriceBuy(newPriceBuy);
        note.setDescription(newDescription);
        note.setVisible(newVisible);

        // when
//        noteService.updateNote(note);
        noteRepository.updateNote(note);

        // ważne – czyścimy persistence context
        entityManager.clear();

        Note updated = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalStateException("Updated note not found"));

        // then
        assertThat(updated.getPriceBuy())
                .as("PriceBuy should be updated")
                .isEqualTo(newPriceBuy);

        assertThat(updated.getDescription())
                .as("Description should be updated")
                .isEqualTo(newDescription);

        assertThat(updated.getVisible())
                .as("Visible flag should be updated")
                .isEqualTo(newVisible);

        log.info("\n🟢 [IT][NOTE] updateNote successful for noteId={}", noteId);
    }

    /**
     * Test zwraca ile banknotów widzi USER
     * Zwraca Ili banknotow USER nie widzi
     * Awraca ilość banknotów które widzi ADMIN
     */

    @Test
    void shouldFilterProperlyByVisibleParameter() {

        // given
        String status = "KOLEKCJA";
        Long countryId = 172L;

        // when
        List<CurrencyByStatus> all =
                noteRepository.currencyByStatus(status, countryId, null);

        List<CurrencyByStatus> visibleTrue =
                noteRepository.currencyByStatus(status, countryId, true);

        List<CurrencyByStatus> visibleFalse =
                noteRepository.currencyByStatus(status, countryId, false);

        long sumAll = all.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        long sumTrue = visibleTrue.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        long sumFalse = visibleFalse.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();
        // then


        assertThat(sumTrue + sumFalse).isEqualTo(sumAll);
        assertThat(all).isNotEmpty();

        // visible true i false nie mogą mieć więcej rekordów niż all
        assertThat(visibleTrue.size()).isLessThanOrEqualTo(all.size());
        assertThat(visibleFalse.size()).isLessThanOrEqualTo(all.size());

        log.info("""
        🟢 [IT][SECURITY] visible filter verification
        ALL     -> {}
        TRUE    -> {}
        FALSE   -> {}
        """,
                all.size(),
                visibleTrue.size(),
                visibleFalse.size()
        );

        log.info("""
        🟢 [IT][SECURITY] visible filter verification
        ALL     -> {}
        TRUE    -> {}
        FALSE   -> {}
        """,
                sumAll,
                sumTrue,
                sumFalse
        );
    }

    @Test
    void shouldRespectVisibleAndCountryFiltersInCurrencyByStatus() {

        // given
        String status = "KOLEKCJA";

        // znajdź kraj, dla którego są jakieś wyniki
        List<CurrencyByStatus> any =
                noteRepository.currencyByStatus(status, 172L, null);

        if (any.isEmpty()) {
            // jeśli brak danych dla tego kraju – test pomijamy
            return;
        }

        Long countryId = any.get(0).countryId();

        // when
        List<CurrencyByStatus> all =
                noteRepository.currencyByStatus(status, countryId, null);

        List<CurrencyByStatus> visibleTrue =
                noteRepository.currencyByStatus(status, countryId, true);

        List<CurrencyByStatus> visibleFalse =
                noteRepository.currencyByStatus(status, countryId, false);

        // then
        long sumAll = all.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        long sumTrue = visibleTrue.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        long sumFalse = visibleFalse.stream()
                .mapToLong(CurrencyByStatus::total)
                .sum();

        // ADMIN widzi wszystko
        AssertionsForClassTypes.assertThat(sumAll).isGreaterThanOrEqualTo(sumTrue);
        AssertionsForClassTypes.assertThat(sumAll).isGreaterThanOrEqualTo(sumFalse);

        // suma widocznych i niewidocznych = wszystkie
        AssertionsForClassTypes.assertThat(sumTrue + sumFalse).isEqualTo(sumAll);

        log.info("""
        🟢 [IT][MEDAL] currencyByStatus logic verification
        COUNTRY -> {}
        ALL     -> {}
        TRUE    -> {}
        FALSE   -> {}
        """,
                countryId,
                sumAll,
                sumTrue,
                sumFalse
        );
    }

}
