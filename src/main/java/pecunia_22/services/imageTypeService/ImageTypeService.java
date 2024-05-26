package pecunia_22.services.imageTypeService;

import org.springframework.stereotype.Service;
import pecunia_22.models.ImageType;

import java.util.List;

@Service
public interface ImageTypeService {
    List<ImageType> getAllImageTypes();
    void saveImageType(ImageType imageType);
    ImageType getImageTypeById(Long id);
    void deleteImageTypeById(Long id);
}
