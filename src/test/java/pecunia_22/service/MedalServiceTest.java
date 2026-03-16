package pecunia_22.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import pecunia_22.exceptions.ResourceNotFoundException;
import pecunia_22.mapper.MedalMapper;
import pecunia_22.models.Medal;
import pecunia_22.models.dto.medal.MedalDto;
import pecunia_22.models.repositories.MedalRepository;
import pecunia_22.services.medalService.MedalServiceImpl;
import pecunia_22.services.userService.CurrentUserService;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MedalServiceTest {

    @Mock
    private MedalRepository medalRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private MedalMapper medalMapper;

    @InjectMocks
    private MedalServiceImpl medalService;

    private Medal medal;
    private MedalDto medalDto;

    @BeforeEach
    void setUp() {
        medal = new Medal();
        medal.setId(1L);

        medalDto = new MedalDto();
    }

    @Test
    void shouldReturnMedalForAdmin() {

        when(currentUserService.isAdmin()).thenReturn(true);
        when(medalRepository.findById(1L)).thenReturn(Optional.of(medal));
        when(medalMapper.toDto(medal)).thenReturn(medalDto);

        MedalDto result = medalService.getMedalDtoById(1L);

        assertNotNull(result);
        log.info("""
                result -> {}
                """,result.getId()
                );
        verify(medalRepository).findById(1L);
    }

    @Test
    void shouldReturnVisibleMedalForUser() {

        when(currentUserService.isAdmin()).thenReturn(false);
        when(medalRepository.findByIdAndVisibleTrue(1L)).thenReturn(Optional.of(medal));
        when(medalMapper.toDto(medal)).thenReturn(medalDto);

        MedalDto result = medalService.getMedalDtoById(1L);

        assertNotNull(result);
        verify(medalRepository).findByIdAndVisibleTrue(1L);
    }

    @Test
    void shouldThrowExceptionWhenMedalNotFound() {

        when(currentUserService.isAdmin()).thenReturn(true);
        when(medalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> medalService.getMedalDtoById(1L));
    }

    @Test
    void shouldThrowExceptionWhenUserAccessInvisibleMedal() {

        when(currentUserService.isAdmin()).thenReturn(false);
        when(medalRepository.findByIdAndVisibleTrue(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> medalService.getMedalDtoById(1L));
    }

    @DisplayName("Admin should receive medal by ID")
    @Test
    void shouldReturnMedalForAdminBis() {

        log.info("Starting test: shouldReturnMedalForAdmin");

        when(currentUserService.isAdmin()).thenReturn(true);
        when(medalRepository.findById(1L)).thenReturn(Optional.of(medal));
        when(medalMapper.toDto(medal)).thenReturn(medalDto);

        MedalDto result = medalService.getMedalDtoById(1L);

        log.info("Returned DTO: {}", result);

        assertThat(result)
                .as("Medal DTO returned for admin")
                .isNotNull();

        verify(medalRepository).findById(1L);
    }

    @DisplayName("User should receive visible medal")
    @Test
    void shouldReturnVisibleMedalForUserBis() {

        log.info("Starting test: shouldReturnVisibleMedalForUser");

        when(currentUserService.isAdmin()).thenReturn(false);
        when(medalRepository.findByIdAndVisibleTrue(1L)).thenReturn(Optional.of(medal));
        when(medalMapper.toDto(medal)).thenReturn(medalDto);

        MedalDto result = medalService.getMedalDtoById(1L);

        log.info("Returned DTO: {}", result);

        assertThat(result)
                .as("Visible medal returned for user")
                .isNotNull();

        verify(medalRepository).findByIdAndVisibleTrue(1L);
    }

    @Test
    void shouldThrowExceptionWhenMedalNotFoundBis() {

        log.info("Starting test: shouldThrowExceptionWhenMedalNotFound");

        when(currentUserService.isAdmin()).thenReturn(true);
        when(medalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medalService.getMedalDtoById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("1");

        log.info("Exception correctly thrown");
    }
}
