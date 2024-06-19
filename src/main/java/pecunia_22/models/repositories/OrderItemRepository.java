package pecunia_22.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.OrderItem;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(value = "SELECT ordItem FROM OrderItem ordItem " +
            " WHERE ordItem.orders.numberOrder = ?1 " +
            " ORDER By ordItem.id")
    List<OrderItem> getOrderItemByNumberOrder(String numberOrder);

    @Query(value = "SELECT ordItem FROM OrderItem ordItem " +
            " WHERE ordItem.orders.id = ?1 " +
            " ORDER By ordItem.id")
    List<OrderItem> getOrderItemByOrderId(Long orderId);
}
