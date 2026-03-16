package pecunia_22.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pecunia_22.exceptions.ResourceNotFoundException;
import pecunia_22.models.dto.medal.MedalDto;
import pecunia_22.services.medalService.MedalService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.springframework.security.test.context.support.WithMockUser;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class MedalSecurityTest {

    @Autowired
    MedalService medalService;

    @Test
    @WithMockUser(authorities = "USER")
    void userShouldNotSeeInvisibleMedal() {

        assertThatThrownBy(() -> medalService.getMedalDtoById(5L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void adminShouldAccessMedal() {

        MedalDto result = medalService.getMedalDtoById(2L);

        assertThat(result).isNotNull();
    }
}
