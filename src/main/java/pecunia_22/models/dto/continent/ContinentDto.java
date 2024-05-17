package pecunia_22.models.dto.continent;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContinentDto {
    @NotNull(message = "Select continent")
    private Long id;
    private String continentEn;
    private String continentPl;
    private String continentCode;
    private Timestamp created_at;
    private Timestamp updated_at;
}
