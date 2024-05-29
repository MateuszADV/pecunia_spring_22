package pecunia_22.models.dto.shippingType;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ShippingTypeDtoSelect {
    @NotNull(message = "Select Sipping")
    private Long id;
    private String shippingTypeEn;
    private String shippingTypePl;
    private Double shippingCost;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
