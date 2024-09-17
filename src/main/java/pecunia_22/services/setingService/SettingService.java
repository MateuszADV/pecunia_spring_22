package pecunia_22.services.setingService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Pattern;
import pecunia_22.models.setting.Setting;

import java.util.List;

@Service
public interface SettingService {
    List<Setting> getAllSetting();
    void saveSetting(Setting setting);
    Setting saveSettingGet(Setting setting);
    Setting getSettingById(Long id);
    void deleteSettingById(Long id);
    Setting getSettingByName(String name);
}
