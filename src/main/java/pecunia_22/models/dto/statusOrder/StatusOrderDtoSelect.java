package pecunia_22.models.dto.statusOrder;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StatusOrderDtoSelect {
    @NotNull(message = "Status Order")
    private Long id;
    private String statusEn;
    private String statusPl;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
