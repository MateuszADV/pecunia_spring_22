package pecunia_22.security.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import pecunia_22.appUser.AppUserService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig  {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
//                        ***********************************
//                        *************API*******************
//                        ***********************************
//                .authorizeRequests()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v*/registration/**")
                        .permitAll()
                .requestMatchers("/api/v*/country")
                .permitAll()
                .requestMatchers("/api/v*/count_country")
                .permitAll()
                .requestMatchers("/api/v*/report/**")
                .permitAll()
                .requestMatchers("/api/v*/report/method**")
                .permitAll()
                .requestMatchers("/api/v*/reportTest/**")
                .permitAll()
//                        ***********************************
//                        *************VIEW******************
//                        ***********************************
                .requestMatchers("/flags/**")
                .permitAll()

                .requestMatchers("/", "/registration", "/about")
                .permitAll()
                .requestMatchers("/country", "/country/show/**")
                .permitAll()
//                .antMatchers("/continent").hasAnyAuthority("USER", "ADMIN")
//                ********************NOTE COLLECTOIN*****************************************
                .requestMatchers("/note/collection/**").hasAnyAuthority("ADMIN", "USER")
//                ********************NOTE FOR SELL ******************************************
                .requestMatchers("/note/forSell/**")
                .permitAll()
//                ********************Coin FOR SELL ******************************************
                .requestMatchers("/coin/forSell/**")
                .permitAll()
//                ********************CONTINENT*****************************************
                .requestMatchers("/continent/**")
                .permitAll()
//                *********************CURRENCY******************************************
                .requestMatchers("/currency", "/currency/show/**", "/currency/list/**")
                .permitAll()
                .requestMatchers("/search")
                .permitAll()
//                .antMatchers("/test").hasAnyAuthority("USER")
                .anyRequest().hasAnyAuthority("ADMIN"));
        http
                .formLogin(withDefaults())
                .httpBasic(withDefaults());
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login"));
////                .formLogin().permitAll()
////                .loginPage("/login").and()
////                .exceptionHandling().accessDeniedPage("/error");
//        http
//                .logout(logout -> logout
//                        .logoutUrl("/")
//                        .addLogoutHandler(new SecurityContextLogoutHandler())
//                );
//                .logout()
//                .permitAll();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/vendor/**","/fonts/**","/reportsChart/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
}
