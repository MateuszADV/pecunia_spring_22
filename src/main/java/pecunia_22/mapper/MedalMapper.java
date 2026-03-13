package pecunia_22.mapper;

import org.modelmapper.ModelMapper;
import pecunia_22.models.Medal;
import pecunia_22.models.dto.medal.MedalDto;
import org.springframework.stereotype.Component;

@Component
public class MedalMapper {
    private final ModelMapper modelMapper;

    public MedalMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MedalDto toDto(Medal medal) {
        return modelMapper.map(medal, MedalDto.class);
    }

    public Medal toEntity(MedalDto dto) {
        return modelMapper.map(dto, Medal.class);
    }
}
