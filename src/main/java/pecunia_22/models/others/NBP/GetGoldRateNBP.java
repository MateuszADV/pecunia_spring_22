package pecunia_22.models.others.NBP;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetGoldRateNBP {
    private String date;
    private Double price;
}
