package pecunia_22.models.rersponse.customerResponse;

import lombok.*;
import pecunia_22.models.dto.customer.CustomerDto;
import pecunia_22.models.dto.order.OrderDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomerOrder {
    private CustomerDto customer;
    private List<OrderDto> orders;
}
