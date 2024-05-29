package pecunia_22.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pecunia_22.models.ShippingType;

public interface ShippingTypeRepository extends JpaRepository<ShippingType, Long> {
}
