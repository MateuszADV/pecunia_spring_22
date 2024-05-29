package pecunia_22.models.dto.shippingType;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ShippingTypeDtoForm {
    private Long id;
    private String shippingTypeEn;
    @NotEmpty(message = "Rodzaj przesyłki musi być podany")
    private String shippingTypePl;
    private Double shippingCost;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
