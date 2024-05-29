package pecunia_22.models.dto.statusOrder;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StatusOrderDtoForm {
    private Long id;
    @NotEmpty(message = "Element nie może być pusty")
    @Size(min = 3, message = "Minimum 3 znaki")
    private String statusEn;
    @NotEmpty(message = "Element nie może być pusty")
    @Size(min = 3, message = "Minimum 3 znaki")
    private String statusPl;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
