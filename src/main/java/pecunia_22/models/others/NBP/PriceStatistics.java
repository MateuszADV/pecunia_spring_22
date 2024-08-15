package pecunia_22.models.others.NBP;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PriceStatistics {

    public PriceStatistics(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    private String name;
    private Double price;
    private String date;
}
