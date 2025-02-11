package pecunia_22.services.medalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pecunia_22.models.Medal;
import pecunia_22.models.repositories.MedalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MedalServiceImpl implements MedalService {

    private MedalRepository medalRepository;

    @Autowired
    public MedalServiceImpl(MedalRepository medalRepository) {
        this.medalRepository = medalRepository;
    }

    @Override
    public List<Medal> getAllMedal() {
        return medalRepository.findAll();
    }

    @Override
    public void saveMedal(Medal medal) {
        this.medalRepository.save(medal);
    }

    @Override
    public Medal getMedalById(Long id) {
        Optional<Medal> optional = medalRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Medal Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteMedalById(Long id) {
        this.medalRepository.deleteById(id);
    }
}
