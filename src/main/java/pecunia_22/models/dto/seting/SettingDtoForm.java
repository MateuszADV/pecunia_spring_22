package pecunia_22.models.dto.seting;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SettingDtoForm {
    private Long id;
    @NotEmpty(message = "Wartość nie może być pusta")
    private String name;
    @NotEmpty(message = "Wartość nie może być pusta")
    private String setting;
    private String parameter;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
