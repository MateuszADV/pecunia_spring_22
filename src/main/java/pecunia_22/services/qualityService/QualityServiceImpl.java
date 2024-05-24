package pecunia_22.services.qualityService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.Quality;
import pecunia_22.models.repositories.QualityRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QualityServiceImpl implements QualityService {

    private QualityRepository qualityRepository;

    @Override
    public List<Quality> getAllQuality() {
        return qualityRepository.findAll();
    }

    @Override
    public void saveQuality(Quality quality) {
        this.qualityRepository.save(quality);
    }


    @Override
    public Quality getQualityById(Long id) {
        Optional<Quality> optional = qualityRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Quality Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteQualityById(Long id) {
        this.qualityRepository.deleteById(id);
    }
}
