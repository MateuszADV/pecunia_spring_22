package pecunia_22.services.pattern;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.Pattern;
import pecunia_22.models.repositories.PatternRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PatternServiceImpl implements PatternService {

    private PatternRepository patternRepository;

    @Override
    public List<Pattern> getAllPattern() {
        return patternRepository.findAll();
    }

    @Override
    public void savePattern(Pattern pattern) {

//        this.patternRepository.save(pattern);
    }

    @Override
    public Pattern savePatternGet(Pattern pattern) {
        Pattern patternSaveGet = patternRepository.save(pattern);
//        return patternSaveGet;
        return this.patternRepository.save(pattern);
    }

    @Override
    public Pattern getPatternById(Long id) {
        Optional<Pattern> pattern = patternRepository.findById(id);
        if (pattern.isPresent()) {
            return pattern.get();
        }else {
            throw new RuntimeException("Pattern Not Found For Id :: " + id);
        }
    }

    @Override
    public void deletePatternById(Long id) {
        this.patternRepository.deleteById(id);
    }

}
