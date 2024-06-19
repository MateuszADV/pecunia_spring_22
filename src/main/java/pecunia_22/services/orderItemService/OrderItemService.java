package pecunia_22.services.orderItemService;

import org.springframework.stereotype.Service;
import pecunia_22.models.OrderItem;

import java.util.List;

@Service
public interface OrderItemService {
    List<OrderItem> getAllOrderItem();
    void saveOrderItem(OrderItem orderItem);
    OrderItem saveOrderItemGet(OrderItem orderItem);
    OrderItem getOrderItemFindById(Long id);
    void deleteOrderItemById(Long id);

    List<OrderItem> getOrderItemByNumberOrder(String numberOrder);
    List<OrderItem> getOrderItemByOrderId(Long orderId);
}
