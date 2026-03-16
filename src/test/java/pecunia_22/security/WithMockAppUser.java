package pecunia_22.security;

import org.springframework.security.test.context.support.WithSecurityContext;
import pecunia_22.appUser.AppUserRole;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAppUserSecurityContextFactory.class)
public @interface WithMockAppUser {
    String email() default "admin@test.com";

    String firstName() default "Admin";

    String lastName() default "Test";

    AppUserRole role() default AppUserRole.ADMIN;
}
