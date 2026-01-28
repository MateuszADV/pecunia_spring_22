package pecunia_22.models.sqlClass;

import lombok.*;
import pecunia_22.models.dto.making.MakingDto;
import pecunia_22.models.dto.pattern.PatternDto;
import pecunia_22.models.dto.quality.QualityDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GetNotesByStatus extends GetByStatus{

    private Long noteId;
    private MakingDto makings;
    private Integer width;
    private Integer height;

    public GetNotesByStatus(
            Long countryId,
            String countryEn,
            String countryPl,
            Long currencyId,
            String currencySeries,
            PatternDto patterns,
            String bought,
            Double denomination,
            String nameCurrency,
            String itemDate,
            Double priceBuy,
            Double priceSell,
            Integer quantity,
            String unitQuantity,
            QualityDto qualities,
            Boolean visible,
            String description,
            String aversPath,
            String reversePath,
            Long noteId,
            MakingDto makings,
            Integer width,
            Integer height
    ) {
        super(
                countryId,
                countryEn,
                countryPl,
                currencyId,
                currencySeries,
                patterns,
                bought,
                denomination,
                nameCurrency,
                itemDate,
                priceBuy,
                priceSell,
                quantity,
                unitQuantity,
                qualities,
                visible,
                description,
                aversPath,
                reversePath
        );
        this.noteId = noteId;
        this.makings = makings;
        this.width = width;
        this.height = height;
    }

}
