package pecunia_22.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
}
