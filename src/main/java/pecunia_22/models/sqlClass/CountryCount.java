package pecunia_22.models.sqlClass;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CountryCount {
    private Long continentId;
    private String continentEn;
    private String continentPl;
    private String code;
    private Integer total;
}
