package pecunia_22.services.userService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {
    private Authentication getAuthentication() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

    @Override
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    @Override
    public boolean isUser() {
        return hasRole("USER");
    }

    private boolean hasRole(String role) {
        return getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    @Override
    public Boolean visibleFilter() {
        return isAdmin() ? null : true;
    }



}
