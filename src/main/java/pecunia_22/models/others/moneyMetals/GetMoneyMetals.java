package pecunia_22.models.others.moneyMetals;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetMoneyMetals {
    private Date time;
    private List<MoneyMetal> moneyMetalList;
}
