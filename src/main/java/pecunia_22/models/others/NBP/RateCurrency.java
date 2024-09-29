package pecunia_22.models.others.NBP;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RateCurrency {
    private String no;
    private String effectiveDate;
    private Double mid;
    private Double bid;
    private Double ask;
}
