package pecunia_22.services.shippingType;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.ShippingType;
import pecunia_22.models.repositories.ShippingTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShippingTypeServiceImpl implements ShippingTypeService {

    private ShippingTypeRepository shippingTypeRepository;

    @Override
    public List<ShippingType> getAllShippingType() {
        return this.shippingTypeRepository.findAll();
    }

    @Override
    public void saveShippingType(ShippingType shippingType) {
        this.shippingTypeRepository.save(shippingType);
    }

    @Override
    public ShippingType saveShippingTypeGet(ShippingType shippingType) {
        return this.shippingTypeRepository.save(shippingType);
    }

    @Override
    public ShippingType getShippingTypeFindById(Long id) {
        Optional<ShippingType> optional = shippingTypeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Shipping Type Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteShippingTypeById(Long id) {
        Optional<ShippingType> optional = shippingTypeRepository.findById(id);
        if (optional.isPresent()) {
            shippingTypeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Element O podamyn id - " + id + " ,który chcesz usunąć nie istnieje");
        }
    }
}
