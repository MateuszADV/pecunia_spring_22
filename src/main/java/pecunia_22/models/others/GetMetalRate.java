package pecunia_22.models.others;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetMetalRate {
    private ApiResponseInfo apiResponseInfo;
    private List<MetalRate> metalRates;
}
