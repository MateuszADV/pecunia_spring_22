package pecunia_22.models.dto.making;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MakingDtoSelect {
    @NotNull(message = "SELECT MAKING")
    private Long id;
    private String making;
    private String makingPl;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
