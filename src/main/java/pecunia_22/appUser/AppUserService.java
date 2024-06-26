package pecunia_22.appUser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pecunia_22.registration.token.ConfirmationToken;
import pecunia_22.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

  private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

  private final AppUserRepository appUserRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ConfirmationTokenService confirmationTokenService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return appUserRepository.findByEmail(email)
            .orElseThrow(() ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
  }

  public String signUpUser(AppUser appUser) {
    Boolean userExists = appUserRepository
            .findByEmail(appUser.getEmail())
            .isPresent();

    if (userExists) {
      throw new IllegalStateException("email already taken");
    }

    String encededPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
    appUser.setPassword(encededPassword);

    appUserRepository.save(appUser);

    String token = UUID.randomUUID().toString();

    ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            appUser
    );

    // ZAkomentowane zapisu tokena dla użytkownika
//    confirmationTokenService.saveConfirmationToken(confirmationToken);

//    TODO: Send Email

    return token;
  }

  public int enableAppUser(String email) {
    return appUserRepository.enableAppUser(email);
  }
}
