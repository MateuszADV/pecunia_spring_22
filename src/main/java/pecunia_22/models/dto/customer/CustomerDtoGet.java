package pecunia_22.models.dto.customer;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomerDtoGet {
    private Long id;
    private String uniqueId;
    private Boolean active;
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
