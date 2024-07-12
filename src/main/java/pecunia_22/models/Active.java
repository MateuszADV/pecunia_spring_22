package pecunia_22.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "actives")
public class Active {
    @SequenceGenerator(
            name = "actives_sequence",
            sequenceName = "actives_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "actives_sequence"
    )
    private Long id;
    @Column(name = "active_cod")
    private Integer activeCod;
    private String name;
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

    @OneToMany(mappedBy = "actives", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Currency> currencies;

    @OneToMany(mappedBy = "actives", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Note> notes;
}
