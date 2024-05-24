package pecunia_22.models.dto.status;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StatusDto {

    private Long id;
    private String status;
    private String statusPl;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
