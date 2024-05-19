package pecunia_22.models.dto.bought;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BoughtDtoForm {
    private Long id;
    @NotEmpty(message = "Wartość nie może być pusta")
    private String name;
    private String fullName;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
