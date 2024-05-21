package pecunia_22.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "makings")
public class Making {
    @SequenceGenerator(
            name = "makings_sequence",
            sequenceName = "makings_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "makings_sequence"
    )
    private Long id;
    private String making;
    @Column(name = "making_pl")
    private String makingPl;
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

//    @OneToMany(mappedBy = "makings", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    private List<Note> notes;
}
