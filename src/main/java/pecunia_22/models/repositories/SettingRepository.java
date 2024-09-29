package pecunia_22.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pecunia_22.models.setting.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    @Query(value = "SELECT seting FROM Setting  seting" +
                   " WHERE seting.name = ?1")
    Setting findSettingByName(String name);

}
