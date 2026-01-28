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
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class NoteNewController {

    private NoteRepository noteRepository;

    @GetMapping("/note/newNotes")
    public String getIndex(ModelMap modelMap) {
        List<Object[]> objects;
        List<CountryByStatus> countryByStatusList = new ArrayList<>();
        objects = noteRepository.countryByStatus("NEW");
        try {
            System.out.println(JsonUtils.gsonPretty(objects));
            for (Object[] object : objects) {
                countryByStatusList.add(new ModelMapper().map(object[0], CountryByStatus.class));
            }

            modelMap.addAttribute("countryByStatusList", countryByStatusList);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "note/newNote/index";
    }

    @GetMapping("/note/newNote/country/notes/{countryId}")
    public String getCountryNotes(@PathVariable("countryId") Long countryId, ModelMap modelMap) {
        System.out.println("[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]C");
        System.out.println(countryId);
        System.out.println("[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]C");

        List<Object[]> objects;
        objects = noteRepository.getNotesByStatus("NEW", countryId);
        List<GetNotesByStatus> getNotesByStatusList = new ArrayList<>();
        for (Object[] object : objects) {
            getNotesByStatusList.add(new ModelMapper().map(object[0],GetNotesByStatus.class));
        }

        modelMap.addAttribute("newNotesList", getNotesByStatusList);
        System.out.println(JsonUtils.gsonPretty(getNotesByStatusList));
        return "note/newNote/notes";
    }

    @GetMapping("/note/newNotes/loc/")
    public String getNewNotesLoc(ModelMap modelMap) {
        List<Object[]> objects = noteRepository.getNotesByStatusAndBought("NEW", "LOC");

        List<GetNotesByStatus> getNotesByStatusList = new ArrayList<>();
//        getNotesByStatusList = noteRepository.getNotesByStatusAndBought("NEW", "LOC");
        for (Object[] object : objects) {
            getNotesByStatusList.add(new ModelMapper().map(object[0],GetNotesByStatus.class));
        }

        modelMap.addAttribute("newNotesList", getNotesByStatusList);

        Double sum = getNotesByStatusList.stream()
                .mapToDouble(i -> i.getQuantity() * i.getPriceBuy())
                .sum();

        modelMap.addAttribute("totalSum", sum);
        System.out.println("((((((((((((((((((((((((((((((SUM)))))))))))))))))))))))))))))))))))))))))))");
        System.out.println(sum);
        System.out.println("((((((((((((((((((((((((((((((SUM)))))))))))))))))))))))))))))))))))))))))))");

        return "note/newNote/loc";
    }
}
