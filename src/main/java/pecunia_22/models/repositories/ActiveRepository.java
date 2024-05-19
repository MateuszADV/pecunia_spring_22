package pecunia_22.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Active;

@Repository
public interface ActiveRepository extends JpaRepository<Active, Long> {
    Boolean existsByActiveCod(Integer active_code);
    Active findByActiveCod(Integer activeCod);
}
