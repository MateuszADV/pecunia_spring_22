package pecunia_22.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pecunia_22.models.StatusOrder;

@Repository
public interface StatusOrderRepository extends JpaRepository<StatusOrder, Long> {
}
