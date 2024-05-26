package pecunia_22.services.imageTypeService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.ImageType;
import pecunia_22.models.repositories.ImageTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageTypeServiceImpl implements ImageTypeService {
    private ImageTypeRepository imageTypeRepository;

    @Override
    public List<ImageType> getAllImageTypes() {
        return this.imageTypeRepository.findAll();
    }

    @Override
    public void saveImageType(ImageType imageType) {
        this.imageTypeRepository.save(imageType);
    }

    @Override
    public ImageType getImageTypeById(Long id) {
        Optional<ImageType> optional = imageTypeRepository.findById(id);
        ImageType imageType = new ImageType();
        if (optional.isPresent()){
            imageType = optional.get();

        }else {
            throw new RuntimeException("Image Type Not Found For Id :: " + id);
        }
        return imageType;
    }

    @Override
    public void deleteImageTypeById(Long id) {
        this.imageTypeRepository.deleteById(id);
    }
}
