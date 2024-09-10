package pecunia_22.models.others.NBP;

import lombok.*;
import pecunia_22.models.others.Rate;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangeCurrency {
    private String table;
    private String currency;
    private String code;
    private List<RateCurrency> rateCurrencies;
}
