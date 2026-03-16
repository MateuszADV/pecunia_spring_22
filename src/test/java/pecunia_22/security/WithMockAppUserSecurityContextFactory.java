package pecunia_22.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import pecunia_22.appUser.AppUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WithMockAppUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockAppUser> {


    private static final Logger log =
            LoggerFactory.getLogger(WithMockAppUserSecurityContextFactory.class);

    @Override
    public SecurityContext createSecurityContext(WithMockAppUser annotation) {

        log.info("Creating mock security context...");

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        AppUser user = new AppUser(
                annotation.firstName(),
                annotation.lastName(),
                annotation.email(),
                "password",
                annotation.role()
        );

        log.info("Mock user created: {} {} ({}) ROLE={}",
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAppUserRole());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities()
                );

        context.setAuthentication(authentication);

        log.info("Authentication set in SecurityContext");

        return context;
    }

//    @Override
//    public SecurityContext createSecurityContext(WithMockAppUser annotation) {
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//
//        AppUser user = new AppUser(
//                annotation.firstName(),
//                annotation.lastName(),
//                annotation.email(),
//                "password",
//                annotation.role()
//        );
//
//        Authentication auth =
//                new UsernamePasswordAuthenticationToken(
//                        user,
//                        user.getPassword(),
//                        user.getAuthorities()
//                );
//
//        context.setAuthentication(auth);
//
//        return context;
//    }
}
