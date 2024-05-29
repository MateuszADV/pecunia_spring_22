package pecunia_22.models.dto.statusOrder;

import lombok.*;

import java.sql.Timestamp;
import java.util.function.ToDoubleBiFunction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StatusOrderDto {
    private Long id;
    private String statusEn;
    private String statusPl;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
