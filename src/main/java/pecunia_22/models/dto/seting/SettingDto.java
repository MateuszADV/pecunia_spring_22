package pecunia_22.models.dto.seting;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SettingDto {
    private Long id;
    private String name;
    private String setting;
    private String parameter;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
