package pecunia_22.models.others;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MetalRate {
    private String symbol;
    private String name;
    private Float price;
    private String updateAt;
    private String updatedAtReadable;
}
