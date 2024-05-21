package pecunia_22.services.makingService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.Making;
import pecunia_22.models.repositories.MakingRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MakingServiceImpl implements MakingService {
    private MakingRepository makingRepository;

    @Override
    public List<Making> getAllMakings() {
        return this.makingRepository.findAll();
    }

    @Override
    public void saveMaking(Making making) {
        this.makingRepository.save(making);
    }

    @Override
    public Making getMakingById(Long id) {
        Optional<Making> optional = makingRepository.findById(id);
        Making making = new Making();
        if (optional.isPresent()) {
            making = optional.get();
        } else {
            throw new RuntimeException("Making Not Found For Id :: " + id);
        }
        return making;
    }

    @Override
    public void deleteMakingById(Long id) {
        this.makingRepository.deleteById(id);
    }
}
