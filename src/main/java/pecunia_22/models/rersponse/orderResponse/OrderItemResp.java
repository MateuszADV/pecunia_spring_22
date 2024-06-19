package pecunia_22.models.rersponse.orderResponse;

import lombok.*;
import pecunia_22.models.dto.order.OrderDto;
import pecunia_22.models.dto.orderItem.OrderItemDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderItemResp {
    private OrderDto orders;
    private List<OrderItemDto> orderItems;
}
