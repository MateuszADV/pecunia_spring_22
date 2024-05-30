package pecunia_22.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "orders")
public class Order {
    @SequenceGenerator(
            name = "orders_sequence",
            sequenceName = "orders_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orders_sequence"
    )
    private Long id;
    @Column(name = "number_order")
    private String numberOrder;
    @Column(name = "date_order")
    private Date dateOrder;
    @Column(name = "date_send_order")
    private Date dateSendOrder;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @Column(name = "shipping_cost")
    private Double shippingCost;
    private String description;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;

    @ManyToOne
    @JoinColumn(name = "status_order_id")
    private StatusOrder statusOrders;

    @ManyToOne
    @JoinColumn(name = "shipping_type_id")
    private ShippingType shippingTypes;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customers;

//    @OneToMany(mappedBy = "orders", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    private List<OrderItem> orderItems;
}
