package pecunia_22.models.others;

import lombok.*;
import pecunia_22.models.others.NBP.Exchange;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetRateCurrencyTableA {
    private ApiResponseInfo apiResponseInfo;
    private Exchange exchange;
}
