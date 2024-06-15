package pecunia_22.models.sqlClass;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyByStatus {
    private Long countryId;
    private String continentEn;
    private String countryEn;
    private String countryPl;
    private Long currencyId;
    private String currencySeries;
    private Integer total;
}
