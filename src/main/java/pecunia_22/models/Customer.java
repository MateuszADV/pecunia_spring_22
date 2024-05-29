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
@Table(name = "customers")
public class Customer {
    @SequenceGenerator(
            name = "customers_sequence",
            sequenceName = "customers_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customers_sequence"
    )
    private Long id;
    @Column(name = "unique_id")
    private String uniqueId;
    private Boolean active;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    private String city;
    @Column(name = "zip_code")
    private String zipCode;
    private String street;
    private String number;
    private String email;
    private String nick;
    private String phone;
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

//    @OneToMany(mappedBy = "customers", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    private List<Order> orders;
}
