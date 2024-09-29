package pecunia_22.models.others.NBP;

import lombok.*;
import pecunia_22.models.others.ApiResponseInfo;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetRateCurrency {
    private ApiResponseInfo apiResponseInfo;
    private List<Exchange> exchangeList;
}
