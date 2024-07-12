package pecunia_22.models.setting;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "settings")
public class Setting {
    @SequenceGenerator(
            name = "settings_sequence",
            sequenceName = "settings_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "settings_sequence"
    )
    private Long id;
    private String name;
    private String setting;
    private String parameter;
    private String description;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
