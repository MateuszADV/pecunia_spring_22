package pecunia_22.services.OrderService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Order;

import java.util.List;

@Service
public interface OrderService {
    List<Order> getAllOrder();
    void saveOrder(Order order);
    Order saveOrderGet(Order order);
    Order getOrderFindById(Long id);
    void deleteOrderById(Long id);

    List<Order> getOrderBycustomer(String customerUUID);
    Order getOrderByNumberOrder(String numberOrder);

    String getLastNumberOrder();
    Boolean checkLastNumberOrder(String lastNumberOrder);
    String returnFirstNumberOrder();
    String getNextNumber(String number);
    Boolean checkYearOrder(String lastNumberOrder);
    String getNextNumberOrder(String lastNumberOrder);

    String getDateOrder();
}
