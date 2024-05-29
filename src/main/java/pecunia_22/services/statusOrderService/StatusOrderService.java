package pecunia_22.services.statusOrderService;

import org.springframework.stereotype.Service;
import pecunia_22.models.StatusOrder;

import java.util.List;

@Service
public interface StatusOrderService {
    List<StatusOrder> getAllStatusOrder();
    void saveStatusOrder(StatusOrder statusOrder);
    StatusOrder saveStatusOrderGet(StatusOrder statusOrder);
    StatusOrder getStatusOrderFindById(Long id);
    void deleteStatusOrderById(Long id);
}
