package pecunia_22.models.dto.Active;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ActiveDtoForm {
    private Long id;
    @NotNull(message = "Pdaj kod actiwe np:(123)")
    private Integer activeCod;
    @NotEmpty(message = "podaj Opis")
    @NotNull(message = "Opis musi być podany")
    private String name;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
