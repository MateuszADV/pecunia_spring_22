package pecunia_22.services.statusOrderService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.StatusOrder;
import pecunia_22.models.repositories.StatusOrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatusOrderServiceImpl implements StatusOrderService {

    private StatusOrderRepository statusOrderRepository;

    @Override
    public List<StatusOrder> getAllStatusOrder() {
        return this.statusOrderRepository.findAll();
    }

    @Override
    public void saveStatusOrder(StatusOrder statusOrder) {
        this.statusOrderRepository.save(statusOrder);
    }

    @Override
    public StatusOrder saveStatusOrderGet(StatusOrder statusOrder) {
        return this.statusOrderRepository.save(statusOrder);
    }

    @Override
    public StatusOrder getStatusOrderFindById(Long id) {
        Optional<StatusOrder> optional = statusOrderRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("SStatus Order Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteStatusOrderById(Long id) {
        Optional<StatusOrder> optional = statusOrderRepository.findById(id);
        if (optional.isPresent()) {
            statusOrderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Element O podamyn id - " + id + " ,który chcesz usunąć nie istnieje");
        }
    }
}
