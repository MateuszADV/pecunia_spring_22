package pecunia_22.services.userService;


import org.springframework.stereotype.Service;

@Service
public interface CurrentUserService {

    boolean isAdmin();

    boolean isUser();

    Boolean visibleFilter();
}
