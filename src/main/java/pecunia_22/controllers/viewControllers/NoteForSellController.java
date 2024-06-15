package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.GetNotesByStatus;
import pecunia_22.services.noteServices.NoteServiceImpl;

import java.util.List;

@Controller
@AllArgsConstructor
public class NoteForSellController {

    private NoteServiceImpl noteService;

    @GetMapping("/note/forSell")
    public String getindex(ModelMap modelMap) {

        List<CountryByStatus> countryByStatusList = noteService.getCountryByStatusNote("FOR SELL");
        modelMap.addAttribute("countryByStatusList", countryByStatusList);
        return "note/forSell/index";
    }

    @GetMapping("/note/forSell/country/notes/{countryId}")
    public String getCountryNotes(@PathVariable("countryId") Long countryId, ModelMap modelMap) {

        List<GetNotesByStatus> getNotesByStatusList = noteService.getNoteByStatus("FOR SELL", countryId);
        modelMap.addAttribute("noteList", getNotesByStatusList);

        return "note/forSell/notes";
    }

    @GetMapping("/note/forSell/list")
    private String getNoteForSellList(ModelMap modelMap) {
//        List<Object[]> objects = noteRepository.getNotesByStatus("FOR SELL");
//        List<GetNotesByStatus> getNotesByStatusList = new ArrayList<>();
//        for (Object[] object : objects) {
//            getNotesByStatusList.add(new ModelMapper().map(object[0],GetNotesByStatus.class));
//        }

        List<GetNotesByStatus> getNotesByStatusList = noteService.getNoteByStatus("FOR SELL");
        modelMap.addAttribute("noteList", getNotesByStatusList);
//        modelMap.addAttribute("forSellNotesList", getNotesByStatusList);
        return "note/forSell/list";
    }
}
