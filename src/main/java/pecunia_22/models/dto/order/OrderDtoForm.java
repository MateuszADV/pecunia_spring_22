package pecunia_22.models.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pecunia_22.models.dto.customer.CustomerDtoGet;
import pecunia_22.models.dto.shippingType.ShippingTypeDtoSelect;
import pecunia_22.models.dto.statusOrder.StatusOrderDtoSelect;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderDtoForm {
    private Long id;
    private String numberOrder;
    private Date dateOrder;
    private Date dateSendOrder;
    private String trackingNumber;
    @Digits(integer=6, fraction = 2, message = "Podaj poprawną cenę (1.23)")
    @DecimalMin(value = "0.00", message = "Wartość nie może być ujemna")
    @NotNull(message = "Wartość nie może być pusta")
    private Double shippingCost;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;

    @NotNull
    @Valid
    private StatusOrderDtoSelect statusOrders;

    @NotNull
    @Valid
    private ShippingTypeDtoSelect shippingTypes;
    private CustomerDtoGet customers;
}
