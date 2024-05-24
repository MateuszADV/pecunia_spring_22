package pecunia_22.services.qualityService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Quality;

import java.util.List;

@Service
public interface QualityService {
    List<Quality> getAllQuality();
    void saveQuality(Quality quality);
    Quality getQualityById(Long id);
    void deleteQualityById(Long id);
}
