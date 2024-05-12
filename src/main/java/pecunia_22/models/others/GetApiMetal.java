package pecunia_22.models.others;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetApiMetal {
    private ApiResponseInfo apiResponseInfo;
    private List<ApiMetal> apiMetals = new ArrayList<>();
}
