package pecunia_22.services.continentService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Continent;

import java.util.List;

@Service
public interface ContinentService {
    List<Continent> getAllContinent();
    void saveContinent(Continent continent);
    Continent getContinentById(Long id);
    void deleteContinentById(Long id);
    Continent getContinentByContinentEn(String continentEn);

}
