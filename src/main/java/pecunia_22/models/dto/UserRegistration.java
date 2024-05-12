package pecunia_22.models.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRegistration {

    private String firstName;
    private String lastName;
    private String password;
    @NonNull
    private String email;
}
