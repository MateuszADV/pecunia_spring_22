package pecunia_22.services.active;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pecunia_22.models.Active;
import pecunia_22.models.repositories.ActiveRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ActiveServiceImpl implements ActiveService {
    private ActiveRepository activeRepository;

    @Override
    public List<Active> getAllActive() {
        return activeRepository.findAll();
    }

    @Override
    public void saveActive(Active active) {
        this.activeRepository.save(active);
    }

    @Override
    public Active getActiveById(Long id) {
        Optional<Active> active = activeRepository.findById(id);
        if (active.isPresent()) {
            return active.get();
        }else {
            throw new RuntimeException("Continent Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteActiveById(Long id) {
        this.activeRepository.deleteById(id);
    }

    @Override
    public Boolean activeCodExist(Integer activeCod) {
        return activeRepository.existsByActiveCod(activeCod);
    }

    @Override
    public Active getActiveByActiveCod(Integer activeCod) {
        Active active = activeRepository.findByActiveCod(activeCod);
        if (active != null) {
            return active;
        }
        return null;
    }
}
