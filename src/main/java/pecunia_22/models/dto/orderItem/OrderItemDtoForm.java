package pecunia_22.models.dto.orderItem;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
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
public class OrderItemDtoForm {
    private Long id;
    private String pattern;
    @DecimalMin(value = "0", message = "Wartość nie może być mniejsza niż 1")
    @NotNull(message = "Wartość nie może być pusta")
    private Integer quantity;
    private String unitQuantity;
    @Digits(integer=6, fraction = 2, message = "Podaj poprawną cenę (1.23)")
    @DecimalMin(value = "0.00", message = "Wartość nie może być ujemna")
    @NotNull(message = "Wartość nie może być pusta")
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
