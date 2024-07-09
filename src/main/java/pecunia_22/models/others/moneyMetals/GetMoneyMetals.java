package pecunia_22.models.others.moneyMetals;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetMoneyMetals {
    private Long time;
    private List<MoneyMetal> moneyMetalList;
}
