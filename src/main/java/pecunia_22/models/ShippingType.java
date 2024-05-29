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
@Table(name = "shipping_types")
public class ShippingType {
    @SequenceGenerator(
            name = "shipping_types_sequence",
            sequenceName = "shipping_types_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shipping_types_sequence"
    )
    private Long id;
    @Column(name = "shipping_type_en")
    private String shippingTypeEn;
    @Column(name = "shipping_type_pl")
    private String shippingTypePl;
    @Column(name = "shipping_cost")
    private Double shippingCost;
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

//    @OneToMany(mappedBy = "shippingTypes", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    private List<Order> orders;
}
