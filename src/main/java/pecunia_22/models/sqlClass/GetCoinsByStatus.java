package pecunia_22.models.sqlClass;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetCoinsByStatus extends GetByStatus{
    private Long coinId;
    private String composition;
    private Double diameter;
    private Double thickness;
    private Double weight;
}
