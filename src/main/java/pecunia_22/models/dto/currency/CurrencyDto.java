package pecunia_22.models.dto.currency;

import lombok.*;
import pecunia_22.models.dto.active.ActiveDtoSelect;
import pecunia_22.models.dto.country.CountryDto;
import pecunia_22.models.dto.pattern.PatternDtoCurrency;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CurrencyDto {
    private Long id;
    private String cod;
    private String pattern;
    private String currency;
    private String change;
    private Integer active;
    private String dataExchange;
    private String currencySeries;
    private String currencyFrom;
    private String converter;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;

    private ActiveDtoSelect actives;
    private PatternDtoCurrency patterns;
    private CountryDto countries;   // Musi byc zakomentowane bo w wyświetlaniu currency dla danego kraju występuje błąd!!!
}
