package pecunia_22.models.dto.country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountrySearchDto {

    private Long id;
    private String countryEn;
    private String countryPl;
}
