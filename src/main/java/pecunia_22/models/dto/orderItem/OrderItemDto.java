package pecunia_22.models.dto.orderItem;

import lombok.*;
import pecunia_22.models.dto.coin.CoinDto;
import pecunia_22.models.dto.country.CountryDto;
import pecunia_22.models.dto.note.NoteDto;
import pecunia_22.models.dto.order.OrderDto;
import pecunia_22.models.dto.security.SecurityDto;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderItemDto {
    private Long id;
    private String pattern;
    private Integer quantity;
    private String unitQuantity;
    private Double finalPrice;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;

    private CountryDto countries;
    private OrderDto orders;
    private NoteDto notes;
    private CoinDto coins;
    private SecurityDto securities;
}
