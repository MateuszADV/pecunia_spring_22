package pecunia_22.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import pecunia_22.exceptions.ResourceNotFoundException;
import pecunia_22.mapper.MedalMapper;
import pecunia_22.models.Medal;
import pecunia_22.models.dto.medal.MedalDto;
import pecunia_22.models.repositories.MedalRepository;
import pecunia_22.services.medalService.MedalServiceImpl;
import pecunia_22.services.userService.CurrentUserService;

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
        when(medalRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> medalService.getMedalDtoById(4L));
    }

    @Test
    void shouldThrowExceptionWhenUserAccessInvisibleMedal() {

        when(currentUserService.isAdmin()).thenReturn(false);
        when(medalRepository.findByIdAndVisibleTrue(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> medalService.getMedalDtoById(1L));
    }
}
