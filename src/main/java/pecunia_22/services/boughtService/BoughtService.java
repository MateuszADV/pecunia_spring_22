package pecunia_22.services.boughtService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Bought;

import java.util.List;

@Service
public interface BoughtService {
    List<Bought> getAllBought();
    List<Bought>getAllOrderById();
    Bought getBoughtById(Long id);
    Bought saveBought(Bought bought);
    void deleteBoughtById(Long id);

    void updateBought(Bought bought);
}
