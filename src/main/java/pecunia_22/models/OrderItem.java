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
@Table(name = "order_items")
public class OrderItem {

    @SequenceGenerator(
            name = "order_items_sequence",
            sequenceName = "order_items_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_items_sequence"
    )
    private Long id;
    private String pattern;
    private Integer quantity;
    @Column(name = "unit_quantity")
    private String unitQuantity;
    @Column(name = "final_price")
    private Double finalPrice;
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country countries;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orders;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note notes;

    @ManyToOne
    @JoinColumn(name = "coin_id")
    private Coin coins;

    @ManyToOne
    @JoinColumn(name = "security_id")
    private Security securities;
}
