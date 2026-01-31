package pecunia_22.services.noteServices;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pecunia_22.models.Note;
import pecunia_22.models.repositories.NoteRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.models.sqlClass.GetNotesByStatus;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    private NoteRepository noteRepository;

    @Override
    public Note getNoteById(Long id) {
        Optional<Note> optional = noteRepository.findById(id);
        Note note = new Note();
        if (optional.isPresent()) {
            note = optional.get();
        }
        else {
            throw new RuntimeException("Note Not Found For Id :: "+ id);
        }
        return note;
    }

    @Override
    public List<Note> getNoteByCurrencyId(Long currencyId) {
        List<Note> notes = noteRepository.getNoteByCurrencyId(currencyId);
        return notes;
    }

    @Override
    public List<Note> getNoteByCurrencyId(Long currencyId, String role) {
        List<Note> notes = new ArrayList<>();
//        List<NoteDto> noteDtoList = new ArrayList<>();
        if (role == "ADMIN") {
            notes = noteRepository.getNoteByCurrencyId(currencyId);
        } else {
            notes = noteRepository.getNoteByCurrencyId(currencyId, true);

        }
        return notes;
    }

    @Override
    public void deleteNoteById(Long id) {
        this.noteRepository.deleteById(id);
    }

    //    *****************************************
    //    ******Query związane z Note *************
    //    *****************************************


    @Override
    public List<CountryByStatus> getCountryByStatus(String continent, String status, String role) {
        List<Object[]> objects = new ArrayList<>();
        List<CountryByStatus> countryByStatusList = new ArrayList<>();

        if (role == "ADMIN") {
            countryByStatusList = noteRepository.countryByStatus(status, continent, null);
            for (Object[] object : objects) {
                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
            }
        } else {
            countryByStatusList = noteRepository.countryByStatus(status, continent, true);
            for (Object[] object : objects) {
                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
            }
        }
        return countryByStatusList;
    }

    @Override
    public List<CurrencyByStatus> getCurrencyByStatus(Long countryId, String status, String role) {
        List<Object[]> objects = new ArrayList<>();
        List<CurrencyByStatus> currencyByStatusList = new ArrayList<>();

        System.out.println("=========================================================");
        System.out.println(status);
        System.out.println("=========================================================");

        if (role == "ADMIN") {
            currencyByStatusList = noteRepository.currencyByStatus(status, countryId, null);
//            for (Object[] object : objects) {
//                currencyByStatusList.add(new ModelMapper().map(object[0], CurrencyByStatus.class));
//            }
        } else {
            currencyByStatusList = noteRepository.currencyByStatus(status, countryId, true);
//            for (Object[] object : objects) {
//                currencyByStatusList.add(new ModelMapper().map(object[0], CurrencyByStatus.class));
//            }
        }
        return currencyByStatusList;
    }

    @Override
    public Page<Note> findNotePaginated(Integer pageNo, Integer pageSize, Long currencyId, String status, String role) {
        List<Note> notes = new ArrayList<>();
        if (role == "ADMIN") {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.noteRepository.notePageable(currencyId, status, pageable);
        } else {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.noteRepository.notePageable(currencyId, status, true, pageable);
        }
    }

    @Override
    public List<GetNotesByStatus> getNoteByStatus(String status) {
        List<Object[]> objects = noteRepository.getNotesByStatus(status, null, null);
        List<GetNotesByStatus> getNotesByStatusList = new ArrayList<>();
        for (Object[] object : objects) {
            getNotesByStatusList.add(new ModelMapper().map(object[0],GetNotesByStatus.class));
        }
        return getNotesByStatusList;
    }

    @Override
    public List<GetNotesByStatus> getNoteByStatus(String status, String statusSell) {
        List<GetNotesByStatus> getNotesByStatusList = new ArrayList<>();
        List<Object[]> objects = noteRepository.getNotesByStatus(status, statusSell, null);

        for (Object[] object : objects) {
            getNotesByStatusList.add(new ModelMapper().map(object[0],GetNotesByStatus.class));
        }
        return getNotesByStatusList;
    }


    @Override
    public List<GetNotesByStatus> getNoteByStatus(String status, Long countryId) {
        List<Object[]> objects = noteRepository.getNotesByStatus(status,null, countryId);
        List<GetNotesByStatus> getNotesByStatusList = new ArrayList<>();
        for (Object[] object : objects) {
            getNotesByStatusList.add(new ModelMapper().map(object[0],GetNotesByStatus.class));
        }
        return getNotesByStatusList;
    }

    @Override
    public List<CountryByStatus> getCountryByStatusNote(String status) {
        List<Object[]> objects;
        List<CountryByStatus> countryByStatusList = new ArrayList<>();

        try {
            countryByStatusList = noteRepository.countryByStatus(status);
//            System.out.println(JsonUtils.gsonPretty(objects));
//            for (Object[] object : objects) {
//                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
//            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return countryByStatusList;
    }

    @Override
    public void updateNote(Note note) {
        noteRepository.updateNote(note.getCurrencies().getId(), note.getDenomination(), note.getDateBuy(), note.getNameCurrency(), note.getSeries(),
                note.getBoughts().getId(), note.getItemDate(), note.getQuantity(), note.getUnitQuantity(), note.getActives().getId(), note.getPriceBuy(), note.getPriceSell(),
                note.getMakings().getId(), note.getQualities().getId(), note.getWidth(), note.getHeight(), note.getStatuses().getId(), note.getImageTypes().getId(),
                note.getStatusSell(), note.getVisible(), note.getDescription(), note.getAversPath(), note.getReversePath(), note.getSerialNumber(),
                note.getId());
    }
}
