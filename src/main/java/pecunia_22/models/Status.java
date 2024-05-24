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
@Table(name = "statuses")
public class Status {
    @SequenceGenerator(
            name = "statuses_sequence",
            sequenceName = "statuses_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "statuses_sequence"
    )
    private Long id;
    private String status;
    @Column(name = "status_pl")
    private String statusPl;
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

//    @OneToMany(mappedBy = "statuses", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    private List<Note> notes;
}
