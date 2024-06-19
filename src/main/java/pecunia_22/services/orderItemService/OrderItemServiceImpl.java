package pecunia_22.services.orderItemService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.OrderItem;
import pecunia_22.models.repositories.OrderItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private OrderItemRepository itemRepository;

    @Override
    public List<OrderItem> getAllOrderItem() {
        return this.itemRepository.findAll();
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        this.itemRepository.save(orderItem);
    }

    @Override
    public OrderItem saveOrderItemGet(OrderItem orderItem) {
        return this.itemRepository.save(orderItem);
    }

    @Override
    public OrderItem getOrderItemFindById(Long id) {
        Optional<OrderItem> optional = itemRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Order Item Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteOrderItemById(Long id) {
        Optional<OrderItem> optional = itemRepository.findById(id);
        if (optional.isPresent()) {
            itemRepository.deleteById(id);
        } else {
            throw new RuntimeException("Element O podamyn id - " + id + " ,który chcesz usunąć nie istnieje");
        }
    }

    @Override
    public List<OrderItem> getOrderItemByNumberOrder(String numberOrder) {
        List<OrderItem> orderItems = itemRepository.getOrderItemByNumberOrder(numberOrder);
        return orderItems;
    }

    @Override
    public List<OrderItem> getOrderItemByOrderId(Long orderId) {
        List<OrderItem> orderItems = itemRepository.getOrderItemByOrderId(orderId);
        return orderItems;
    }
}
