package pecunia_22.models.dto.customer;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomerDtoForm {
    private Long id;
    private String uniqueId;
    private Boolean active;
    @NotEmpty(message = "Wartość nie może być pusta")
    private String name;
    private String lastName;
    private String city;
    private String zipCode;
    private String street;
    private String number;
    private String email;
    private String nick;
    private String phone;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
