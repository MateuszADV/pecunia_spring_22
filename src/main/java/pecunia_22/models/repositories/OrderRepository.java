package pecunia_22.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT ord FROM Order ord " +
            " WHERE ord.customers.uniqueId = ?1 " +
            " ORDER By ord.id")
    List<Order> getOrderbyCustomerUUID(String customerUUID);

    @Query(value = "SELECT ord FROM Order ord " +
            " WHERE ord.numberOrder = ?1 " +
            " ORDER By ord.id")
    Optional<Order> getOrderByNumberOrder(String numberOrder);

    @Query(value = "SELECT * FROM orders  " +
            " ORDER BY id DESC Limit 1", nativeQuery = true)
    Order getLastOrder();
}
