package pecunia_22.models.dto.making;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MakingDto {
    private Long id;
    private String making;
    private String makingPl;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
