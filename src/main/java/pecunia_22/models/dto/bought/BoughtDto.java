package pecunia_22.models.dto.bought;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BoughtDto {
    private Long id;
    private String name;
    private String fullName;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
