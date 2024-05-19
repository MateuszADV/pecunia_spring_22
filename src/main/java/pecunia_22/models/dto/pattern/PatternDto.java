package pecunia_22.models.dto.pattern;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PatternDto {
    private Long id;
    @Pattern(regexp = "[A-Z]*", message = "Muszą być duze litery (NOTE, COIN, BOND...) ")
    @NotEmpty(message = "Pole nie może być puste (NOTE, COIN, BOND...) ")
    private String pattern;
    private String name;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
