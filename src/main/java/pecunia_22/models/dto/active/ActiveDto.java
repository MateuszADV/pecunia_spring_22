package pecunia_22.models.dto.active;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ActiveDto {
    private Long id;
    private Integer activeCod;
    private String name;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
