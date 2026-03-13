package pecunia_22.services.userService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getUsername() {
        Authentication auth = getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    @Override
    public Set<String> getRoles() {
        Authentication auth = getAuthentication();

        return auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAdmin() {
        return getRoles().contains("ADMIN");
    }

    @Override
    public boolean isUser() {
        return getRoles().contains("USER");
    }

    @Override
    public boolean isLoggedIn() {
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated();
    }
}
