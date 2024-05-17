package pecunia_22.models.dto.Active;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ActiveDtoSelect {
    @NotNull(message = "Select Active")
    private Long id;
    private Integer activeCod;
    private String name;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}