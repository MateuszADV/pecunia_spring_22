package pecunia_22.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Medal;

import java.util.List;

@Repository
public interface MedalRepository extends JpaRepository<Medal, Long> {
    @Query(value = "SELECT medal FROM Medal medal " +
            " WHERE medal.currencies.id = ?1")
    List<Medal> getMedalByCurrencyId(Long currencyId);

    @Query(value = "SELECT medal FROM Medal medal " +
            " WHERE medal.currencies.id = ?1 AND medal.visible = ?2")
    List<Medal> getMedalByCurrencyId(Long currencyId, Boolean visible);
}
