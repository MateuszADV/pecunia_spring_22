package pecunia_22.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.models.Note;
import pecunia_22.models.repositories.NoteRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NoteRepositoryIT {
    @Autowired
    private NoteRepository noteRepository;

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
        log.info("🟢 [IT][NOTE] getNotesByStatus -> {} rows (status={})",
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
        log.info("🟢 [IT][NOTE] getNotesByStatus (no filters) -> {} rows (status={})", resultNoFilter.size(), status);
        assertThat(resultNoFilter).isNotEmpty();
    }

    private static void getInfo(List<Object[]> result, String status, String excludedStatusSell, Long countryId) {
        log.info("🟢 [IT][NOTE] getNotesByStatus (custom query) -> {} rows (status={}, excludedStatusSell={}, countryId={})",
                result.size(), status, excludedStatusSell, countryId);
    }

}
