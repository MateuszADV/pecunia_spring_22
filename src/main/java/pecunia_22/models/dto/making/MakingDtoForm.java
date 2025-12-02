package pecunia_22.models.dto.making;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MakingDtoForm {
    private Long id;
    @NotEmpty(message = "Wartość nie może być pusta")
    private String making;
    @NotEmpty(message = "Wartość nie może być pusta")
    private String makingPl;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
