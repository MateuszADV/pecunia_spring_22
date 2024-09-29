package pecunia_22.services.setingService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.repositories.SettingRepository;
import pecunia_22.models.setting.Setting;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SettingServiceImpl implements SettingService {

    private SettingRepository settingRepository;

    @Override
    public List<Setting> getAllSetting() {
        return settingRepository.findAll();
    }

    @Override
    public void saveSetting(Setting setting) {
        this.settingRepository.save(setting);
    }

    @Override
    public Setting saveSettingGet(Setting setting) {
        return null;
    }

    @Override
    public Setting getSettingById(Long id) {
        Optional<Setting> setting = settingRepository.findById(id);
        if (setting.isPresent()) {
            return setting.get();
        }else {
            throw new RuntimeException("Setting Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteSettingById(Long id) {
        this.settingRepository.deleteById(id);
    }

    @Override
    public Setting getSettingByName(String name) {
        Optional<Setting> setting = Optional.ofNullable(settingRepository.findSettingByName(name));
        if (setting.isPresent()) {
            return setting.get();
        } else {
            throw new RuntimeException("Setting Not Found For name :: " + name);
        }
    }
}
