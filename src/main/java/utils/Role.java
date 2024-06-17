package utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Role {
    public static String role() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toArray()[0].toString();
        System.out.println("------------------------ROLE----------------------------------");
        System.out.println(role);
//        System.out.println(authentication.toString());
//        System.out.println(authentication.getDetails().toString());
//        System.out.println(authentication.getName());
//        System.out.println(authentication.getCredentials());
        System.out.println("------------------------ROLE----------------------------------");
        return role;
    }
}
