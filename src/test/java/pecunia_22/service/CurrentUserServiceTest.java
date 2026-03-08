package pecunia_22.service;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import pecunia_22.services.userService.CurrentUserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class CurrentUserServiceTest {


    private final CurrentUserServiceImpl currentUserService;

    @Autowired
    public CurrentUserServiceTest(CurrentUserServiceImpl currentUserService) {
        this.currentUserService = currentUserService;
    }

    @Test
    void shouldReturnTrueWhenUserIsAdmin() {

        var auth = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        boolean result = currentUserService.isAdmin();

        log.info("""
                Role [isAdmin][ADMIN] -> {}
                """,
                result);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUserIsUser() {

        var auth = new UsernamePasswordAuthenticationToken(
                "user",
                null,
                List.of(new SimpleGrantedAuthority("USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        boolean result = currentUserService.isAdmin();

        log.info("""
                Role [isAdmin][USER] -> {}
                """,
                result);

        assertFalse(result);
    }
}
