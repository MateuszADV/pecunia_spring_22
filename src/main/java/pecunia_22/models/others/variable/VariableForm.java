package pecunia_22.models.others.variable;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import pecunia_22.models.dto.ImageType.ImageTypeDtoSelect;
import pecunia_22.models.dto.active.ActiveDtoSelect;
import pecunia_22.models.dto.bought.BoughtDto;
import pecunia_22.models.dto.currency.CurrencyDto;
import pecunia_22.models.dto.making.MakingDtoSelect;
import pecunia_22.models.dto.quality.QualityDtoSelect;
import pecunia_22.models.dto.status.StatusDtoSelect;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class VariableForm {
    public static void variableToSelect(ModelMap modelMap, List<CurrencyDto> currencyDtos, List<BoughtDto> boughtDtos, List<ActiveDtoSelect> activeDtoSelects, List<MakingDtoSelect> makingDtoSelects, List<QualityDtoSelect> qualityDtoSelects, List<StatusDtoSelect> statusDtoSelects, List<ImageTypeDtoSelect> imageTypeDtoSelects) {
        modelMap.addAttribute("currencies", currencyDtos);
        modelMap.addAttribute("boughts", boughtDtos);
        modelMap.addAttribute("actives", activeDtoSelects);
        modelMap.addAttribute("makings", makingDtoSelects);
        modelMap.addAttribute("qualities", qualityDtoSelects);
        modelMap.addAttribute("statuses", statusDtoSelects);
        modelMap.addAttribute("imageTypes", imageTypeDtoSelects);
        modelMap.addAttribute("standartDate", Date.valueOf(LocalDate.now()));
    }
}
