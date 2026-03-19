package pecunia_22.appUser;

public class AppUserMapper {
    public static AppUserDTO toDTO(AppUser user) {
        return new AppUserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAppUserRole().name()
        );
    }
}
