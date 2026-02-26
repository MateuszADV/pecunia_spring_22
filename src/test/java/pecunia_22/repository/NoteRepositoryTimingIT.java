package pecunia_22.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.models.*;
import pecunia_22.models.repositories.*;
import pecunia_22.testUtils.NoteTestUtils;

import java.util.List;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NoteRepositoryTimingIT {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private BoughtRepository boughtRepository;
    @Autowired
    private ActiveRepository activeRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private QualityRepository qualityRepository;
    @Autowired
    private MakingRepository makingRepository;
    @Autowired
    private ImageTypeRepository imageTypeRepository;

    @Test
    void testNoteRepositoryTiming() {
        // wywołanie kilku metod repozytorium
        List<Note> notes = noteRepository.findAll();
        noteRepository.getNoteByCurrencyId(null);
        log.info("Iloś Pobranych Elementów -> {}",
                notes.size());

        // prosty insert/update żeby zobaczyć czas
//        Note note = new Note();
//        note.setDescription("Test Timing");
//        noteRepository.save(note);
    }
    @Test
    void testNoteRepositoryTimingWithFullNote() {
        // zakładamy, że te obiekty istnieją w bazie testowej
        Currency currency = currencyRepository.findById(1L).get();
        Bought bought = boughtRepository.findById(1L).get();
        Active active = activeRepository.findById(1L).get();
        Status status = statusRepository.findById(1L).get();
        Quality quality = qualityRepository.findById(1L).get();
        Making making = makingRepository.findById(1L).get();
        ImageType imageType = imageTypeRepository.findById(1L).get();

        Note note = NoteTestUtils.createSampleNote(
                currency, bought, active, status, quality, making, imageType
        );

        noteRepository.save(note);
    }
}
