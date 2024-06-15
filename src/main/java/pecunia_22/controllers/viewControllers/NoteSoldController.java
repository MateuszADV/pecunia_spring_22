package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.GetNotesByStatus;
import pecunia_22.services.noteServices.NoteServiceImpl;
import utils.JsonUtils;

import java.util.List;

@Controller
@AllArgsConstructor
public class NoteSoldController {

    private NoteServiceImpl noteService;

    @GetMapping("/note/sold")
    public String getindex(ModelMap modelMap) {

        List<CountryByStatus> countryByStatusList = noteService.getCountryByStatusNote("SOLD");
        modelMap.addAttribute("countryByStatusList", countryByStatusList);
        return "note/sold/index";
    }

    @GetMapping("/note/sold/country/notes/{countryId}")
    public String getCountryNotes(@PathVariable("countryId") Long countryId, ModelMap modelMap) {
        List<GetNotesByStatus> getNotesByStatusList = noteService.getNoteByStatus("SOLD", countryId);
        modelMap.addAttribute("noteList", getNotesByStatusList);
        System.out.println(JsonUtils.gsonPretty(getNotesByStatusList));
        return "note/sold/notes";
    }
}
