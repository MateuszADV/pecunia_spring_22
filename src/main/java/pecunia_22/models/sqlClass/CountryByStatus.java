package pecunia_22.models.sqlClass;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CountryByStatus {
    private String continent;
    private Long countryId;
    private String continentCode;
    private String countryEn;
    private String countryPl;
    private Integer total;
}
