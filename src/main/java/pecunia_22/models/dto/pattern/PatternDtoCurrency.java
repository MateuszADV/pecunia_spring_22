package pecunia_22.models.dto.pattern;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PatternDtoCurrency {
    @NotNull(message = "Select Pattern")
    private Long id;
    private String pattern;
    private String name;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
