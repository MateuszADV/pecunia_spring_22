package pecunia_22.services.boughtService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.Bought;
import pecunia_22.models.repositories.BoughtRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BoughtServiceImpl implements BoughtService {
    private BoughtRepository boughtRepository;

    @Override
    public List<Bought> getAllBought() {
        return boughtRepository.findAll();
    }

    @Override
    public List<Bought> getAllOrderById() {
        return boughtRepository.getAllOrOrderById();
    }

    @Override
    public Bought saveBought(Bought bought) {
        return this.boughtRepository.save(bought);
    }

    @Override
    public Bought getBoughtById(Long id) {
        Optional<Bought> bought = boughtRepository.findById(id);
        if (bought.isPresent()) {
            return bought.get();
        } else {
            throw new RuntimeException("Bought Not Found For Id :: " + id);
        }
    }

    @Override
    public void updateBought(Bought bought) {
        if (boughtRepository.existsById(bought.getId())) {
            boughtRepository.updateBought(bought.getName(), bought.getFullName(), bought.getDescription(), bought.getId());
        }else   {
            throw new RuntimeException("Podczas aktualizacji danych wystąpił bład (id - " + bought.getId() + " nie istnieje)");
        }
    }

    @Override
    public void deleteBoughtById(Long id) {
        if (boughtRepository.existsById(id)) {
            this.boughtRepository.deleteById(id);
        }else   {
            throw new RuntimeException("record o (id - " + id + " który chcesz usunąć nie istnieje)");
        }

    }
}
