package pecunia_22.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class UserCheckLoged {

    public Authentication UserCheckLoged(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication;
    }
}
