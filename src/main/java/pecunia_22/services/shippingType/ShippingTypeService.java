package pecunia_22.services.shippingType;

import org.springframework.stereotype.Service;
import pecunia_22.models.ShippingType;

import java.util.List;

@Service
public interface ShippingTypeService {
    List<ShippingType> getAllShippingType();
    void saveShippingType(ShippingType shippingType);
    ShippingType saveShippingTypeGet(ShippingType shippingType);
    ShippingType getShippingTypeFindById(Long id);
    void deleteShippingTypeById(Long id);
}
