package pecunia_22.models.dto.note;

public record NoteUserDto(
        Long id,
        String nameCurrency,
        Double priceSell,
        Integer quantity,
        String unitQuantity,
        String description,
        String aversPath,
        String reversePath
) {}
