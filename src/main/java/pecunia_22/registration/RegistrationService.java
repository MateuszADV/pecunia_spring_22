package pecunia_22.registration;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.appUser.AppUser;
import pecunia_22.appUser.AppUserRole;
import pecunia_22.appUser.AppUserService;
import pecunia_22.registration.token.ConfirmationToken;
import pecunia_22.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

  private final EmailValidator emailValidator;
  private final AppUserService appUserService;
  private final ConfirmationTokenService confirmationTokenService;
//  private final EmailSender emailSender;

  public String register(RegistrationRequest request) {
    Boolean isValidEmail = emailValidator.test(request.getEmail());

    if (!isValidEmail) {
      throw new IllegalStateException("email not valid");
    }

    final String token = appUserService.signUpUser(
            new AppUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    AppUserRole.USER
            )
    );
//    String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
//    emailSender.send(
//            request.getEmail(),
//            buildEmail(request.getFirstName(), link));
    return token;
  }

  @Transactional
  public String confirmToken(String token) {
    ConfirmationToken confirmationToken = confirmationTokenService
            .getToken(token)
            .orElseThrow(() ->
                    new IllegalStateException("token not found"));

    if (confirmationToken.getConfirmedAt() != null) {
      throw new IllegalStateException("email already confirmed");
    }

    LocalDateTime expiredAt = confirmationToken.getExpiresAt();

    if (expiredAt.isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("token expired");
    }

    confirmationTokenService.setConfirmedAt(token);
    appUserService.enableAppUser(
            confirmationToken.getAppUser().getEmail());
    return "confirmed";
  }
}