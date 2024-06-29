package pecunia_22.models.others;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetMetalSymbol {
    private ApiResponseInfo apiResponseInfo;
    private List<MetalSymbol> metalSymbols;
}
