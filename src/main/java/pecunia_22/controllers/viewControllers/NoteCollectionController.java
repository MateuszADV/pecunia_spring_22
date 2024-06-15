package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pecunia_22.models.Note;
import pecunia_22.models.dto.note.NoteDto;
import pecunia_22.models.repositories.CurrencyRepository;
import pecunia_22.models.repositories.NoteRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.security.config.UserCheckLoged;
import pecunia_22.services.countryService.CountryServiceImpl;
import pecunia_22.services.noteServices.NoteServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NoteCollectionController {

    private NoteServiceImpl noteService;
    private CountryServiceImpl countryService;
    private UserCheckLoged userCheckLoged;

    private NoteRepository noteRepository;
    private CurrencyRepository currencyRepository;

    @Autowired
    public NoteCollectionController(NoteServiceImpl noteService, CountryServiceImpl countryService, UserCheckLoged userCheckLoged, NoteRepository noteRepository) {
        this.noteService = noteService;
        this.countryService = countryService;
        this.userCheckLoged = userCheckLoged;
        this.noteRepository = noteRepository;
    }


    @GetMapping("/note/collection")
    public String getIndex(ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
        System.out.println(JsonUtils.gsonPretty(userCheckLoged.UserCheckLoged()));
        System.out.println(role);
        modelMap.addAttribute("continents", countryService.countryCounts());

        return "note/collection/index";
    }

    @GetMapping("/note/collection/country/")
    public String getCountry(@RequestParam("selectContinent") String continentEn,
                             ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();

        System.out.println(continentEn);
        List<CountryByStatus> countryByStatusList = new ArrayList<>();
        if (role == "ADMIN") {
            countryByStatusList = noteService.getCountryByStatus(continentEn, "KOLEKCJA", role);
        } else {
            countryByStatusList = noteService.getCountryByStatus(continentEn, "KOLEKCJA", role);
            System.out.println("Brak uprawnień to tu");
            modelMap.addAttribute("error", "Brak Uprawnień");
//            return "error";
        }
        modelMap.addAttribute("countryByStatusList", countryByStatusList);


        System.out.println(JsonUtils.gsonPretty(countryByStatusList));

        return "note/collection/country";
    }

    @GetMapping("/note/collection/currency/")
    public String getCurrency(@RequestParam("selectCountryId") Long countryId, ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();

        System.out.println(countryId);
//        List<Object[]> objects = noteRepository.currencyByStatus("KOLEKCJA", countryId);
        List<CurrencyByStatus> currencyByStatusList = new ArrayList<>();
        if (role == "ADMIN") {
            currencyByStatusList = noteService.getCurrencyByStatus(countryId, "KOLEKCJA", role);
        } else {
            currencyByStatusList = noteService.getCurrencyByStatus(countryId, "KOLEKCJA", role);
        }
        modelMap.addAttribute("currencyByStatusList", currencyByStatusList);
        System.out.println(JsonUtils.gsonPretty(currencyByStatusList));
        return "note/collection/currency";
    }

    @GetMapping("/note/collection/notes/")
    public String getNote(@RequestParam("selectCurrencyId") Long currencyId, ModelMap modelMap) {
//        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
//        List<Note> notes = new ArrayList<>();
//        List<NoteDto> noteDtos = new ArrayList<>();
//
//        if (role == "ADMIN") {
//            notes = noteService.getNoteByCurrencyId(currencyId, role);
//            for (Note note : notes) {
//                noteDtos.add(new ModelMapper().map(note, NoteDto.class));
//            }
//        } else {
//            notes = noteService.getNoteByCurrencyId(currencyId, role);
//            if (notes.size() > 0) {
//                for (Note note : notes) {
//                    noteDtos.add(new ModelMapper().map(note, NoteDto.class));
//                }
//            } else {
//                modelMap.addAttribute("error" , "Brak danych do wyświetlenia!!!");
//                return "error";
//            }
//        }
//        System.out.println(JsonUtils.gsonPretty(noteDtos));
//
//        modelMap.addAttribute("notes", noteDtos);


//        return "note/collection/notes";
        return findPaginated(1, currencyId, "KOLEKCJA", modelMap);
    }

    @GetMapping("/note/collection/notes/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("currencyId") Long currencyId,
                                @RequestParam("status") String status, ModelMap modelMap) {
        String role = userCheckLoged.UserCheckLoged().getAuthorities().toArray()[0].toString();
        Integer pageSize =10;

        Page<Note> page = noteService.findNotePaginated(pageNo, pageSize, currencyId, status, role);
        List<NoteDto> noteDtoList = new ArrayList<>();

        for (Note note : page.getContent()) {
            noteDtoList.add(new ModelMapper().map(note, NoteDto.class));
        }

        String pathPage = "/note/collection/notes/page/";
        modelMap.addAttribute("currentPage", pageNo);
        modelMap.addAttribute("totalPages", page.getTotalPages());
        modelMap.addAttribute("totalItems", page.getTotalElements());
        modelMap.addAttribute("pageSize", pageSize);
        modelMap.addAttribute("pathPage", pathPage);

        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getSize());

        modelMap.addAttribute("notes", noteDtoList);
        System.out.println(role);
        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        return "note/collection/notes";
    }

    @GetMapping("/note/collection/show/{noteId}")
    public String getShow(@PathVariable Long noteId, ModelMap modelMap) {
        System.out.println("pppppppppppppppppppppppppppppppppppppppppppp");
        System.out.println(noteId);
        System.out.println("pppppppppppppppppppppppppppppppppppppppppppp");

        Note note = noteService.getNoteById(noteId);
        NoteDto noteDto = new ModelMapper().map(note, NoteDto.class);
        modelMap.addAttribute("note", noteDto);
        System.out.println(noteDto.getCurrencies().getId());

        return "note/collection/show";
    }
}
