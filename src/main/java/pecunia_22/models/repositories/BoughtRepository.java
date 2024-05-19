package pecunia_22.models.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Bought;

import java.util.List;

@Repository
@Transactional
public interface BoughtRepository extends JpaRepository<Bought, Long> {
    @Query(value = "SELECT bou FROM Bought bou ORDER BY bou.id")
    List<Bought> getAllOrOrderById();

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE boughts SET name = :name, full_name = :fullName, description = :description " +
                    "WHERE id = :id")
    Integer updateBought(@Param("name") String name,
                         @Param("fullName") String fullName,
                         @Param("description") String description,
                         @Param("id") Long id);
}
