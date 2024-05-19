package pecunia_22.models.dto.bought;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BoughtDtoSelect {
    @NotNull(message = "Select Bought")
    private Long id;
    private String name;
    private String fullName;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
