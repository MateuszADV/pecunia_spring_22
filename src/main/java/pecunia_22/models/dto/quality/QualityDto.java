package pecunia_22.models.dto.quality;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class QualityDto {
    private Long id;
    private String quality;
    private String qualityPl;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
