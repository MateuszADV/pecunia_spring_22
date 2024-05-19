package pecunia_22.models;

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
@Table(name = "boughts")
public class Bought {
    @SequenceGenerator(
            name = "boughts_sequence",
            sequenceName = "boughts_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "boughts_sequence"
    )
    private Long id;
    private String name;
    @Column(name = "full_name")
    private String fullName;
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

//    @OneToMany(mappedBy = "boughts", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    private List<Note> notes;
}
