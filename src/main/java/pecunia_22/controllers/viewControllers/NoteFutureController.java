package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pecunia_22.models.repositories.NoteRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.GetNotesByStatus;
import pecunia_22.services.noteServices.NoteServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class NoteFutureController {

    private NoteRepository noteRepository;
    private NoteServiceImpl noteService;

    @GetMapping("/note/future")
    public String getindex(ModelMap modelMap) {
        List<Object[]> objects;
        List<CountryByStatus> countryByStatusList = new ArrayList<>();

        try {
            countryByStatusList = noteService.getCountryByStatusNote("FUTURE");
//            countryByStatusList = noteRepository.countryByStatus("FUTURE");
//            System.out.println(JsonUtils.gsonPretty(objects));
//            for (Object[] object : objects) {
//                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
//            }
//
            modelMap.addAttribute("countryByStatusList", countryByStatusList);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "note/futureNote/index";
    }

    @GetMapping("/note/future/country/notes/{countryId}")
    public String getCountryNotes(@PathVariable("countryId") Long countryId, ModelMap modelMap) {
        System.out.println("[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]C");
        System.out.println(countryId);
        System.out.println("[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]C");

        List<Object[]> objects;
        objects = noteRepository.getNotesByStatus("FUTURE",null, countryId);
        System.out.println(JsonUtils.gsonPretty(objects));
        List<GetNotesByStatus> getNotesByStatusList = new ArrayList<>();
        for (Object[] object : objects) {
            getNotesByStatusList.add(new ModelMapper().map(object[0],GetNotesByStatus.class));
        }

        modelMap.addAttribute("futureNotesList", getNotesByStatusList);
        System.out.println(JsonUtils.gsonPretty(getNotesByStatusList));
        return "note/futureNote/notes";
    }
}
