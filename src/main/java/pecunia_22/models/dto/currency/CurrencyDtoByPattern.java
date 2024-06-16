package pecunia_22.models.dto.currency;

import lombok.*;
import pecunia_22.models.dto.coin.CoinDtoByCurrency;
import pecunia_22.models.dto.country.CountryDto;
import pecunia_22.models.dto.note.NoteDtoByCurrency;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CurrencyDtoByPattern {
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

    private CountryDto countries;

    /*
    Wyświetlanie walut według rodzaju (Pattern)
     */
    private List<NoteDtoByCurrency> notes;
    private List<CoinDtoByCurrency> coins;
//    private List<SecurityDtoByCurrency> securities;
}
