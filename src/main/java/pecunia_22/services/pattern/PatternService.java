package pecunia_22.services.pattern;

import lombok.Setter;
import org.springframework.stereotype.Service;
import pecunia_22.models.Pattern;

import java.util.List;

@Service
public interface PatternService {
    List<Pattern> getAllPattern();
    void savePattern(Pattern pattern);
    Pattern savePatternGet(Pattern pattern);
    Pattern getPatternById(Long id);
    void deletePatternById(Long id);
}
