package pecunia_22.models.sqlClass;

import lombok.*;
import pecunia_22.models.dto.pattern.PatternDto;
import pecunia_22.models.dto.quality.QualityDto;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetCoinsByStatus extends GetByStatus{

    private Long coinId;
    private String composition;
    private Double diameter;
    private Double thickness;
    private Double weight;

    public GetCoinsByStatus(
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
            Long coinId,
            String composition,
            Double diameter,
            Double thickness,
            Double weight
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
        this.coinId = coinId;
        this.composition = composition;
        this.diameter = diameter;
        this.thickness = thickness;
        this.weight = weight;
    }
}
