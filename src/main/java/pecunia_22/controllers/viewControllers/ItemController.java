package pecunia_22.controllers.viewControllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import pecunia_22.models.sqlClass.GetCoinsByStatus;
import pecunia_22.models.sqlClass.GetNotesByStatus;
import pecunia_22.models.sqlClass.GetSecuritiesByStatus;
import pecunia_22.services.coinService.CoinServiceImpl;
import pecunia_22.services.noteServices.NoteServiceImpl;
import pecunia_22.services.securityService.SecurityServiceImpl;

import java.util.List;

@Controller
@AllArgsConstructor
public class ItemController {

    private CoinServiceImpl coinService;
    private NoteServiceImpl noteService;
    private SecurityServiceImpl securityService;

    @GetMapping("/itemForSell")
    public String getForSell(ModelMap modelMap) {
        List<GetCoinsByStatus> getCoinsByStatusList = coinService.getCoinsByStatus("FOR SELL");
        List<GetNotesByStatus> getNotesByStatusList = noteService.getNoteByStatus("FOR SELL");
        List<GetSecuritiesByStatus> getSecuritiesByStatusList = securityService.getSecurityByStatus("FOR SELL");

        modelMap.addAttribute("forSellNotesList", getNotesByStatusList);
        modelMap.addAttribute("forSellCoinsList", getCoinsByStatusList);
        modelMap.addAttribute("forSellSecuritiesList", getSecuritiesByStatusList);
        modelMap.addAttribute("title", "Item For Sell");
        return "item/index";
    }

    @GetMapping("/itemOnDisplay")
    public String getOnDisplay(ModelMap modelMap) {
        List<GetCoinsByStatus> getCoinsByStatusList = coinService.getCoinsByStatus("FOR SELL", "");
        List<GetNotesByStatus> getNotesByStatusList = noteService.getNoteByStatus("FOR SELL", "");

        modelMap.addAttribute("forSellNotesList", getNotesByStatusList);
        modelMap.addAttribute("forSellCoinsList", getCoinsByStatusList);
        modelMap.addAttribute("title", "Item On Display");

        return "item/index";
    }

    @GetMapping("/itemSold")
    public String getSold(ModelMap modelMap) {
        List<GetCoinsByStatus> getCoinsByStatusList = coinService.getCoinsByStatus("SOLD");
        List<GetNotesByStatus> getNotesByStatusList = noteService.getNoteByStatus("SOLD");

        modelMap.addAttribute("forSellNotesList", getNotesByStatusList);
        modelMap.addAttribute("forSellCoinsList", getCoinsByStatusList);
        modelMap.addAttribute("title", "Item Sold");

        return "item/index";
    }
}
