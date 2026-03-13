package pecunia_22.services.userService;


import org.springframework.stereotype.Service;

import java.util.Set;

public interface CurrentUserService {

    String getUsername();

    Set<String> getRoles();

    boolean isAdmin();

    boolean isUser();

    boolean isLoggedIn();
}
