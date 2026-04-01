package pecunia_22.models.dto.currency;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CurrencyDtoWithCount {
    private Long id;
    private String currencySeries;
    private String pattern;
    private String countryEn;

    private Long elementsCount;
}
