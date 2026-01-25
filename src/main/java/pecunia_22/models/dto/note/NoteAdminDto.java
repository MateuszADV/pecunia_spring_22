package pecunia_22.models.dto.note;

import lombok.*;
import pecunia_22.models.dto.CommonDto;
import pecunia_22.models.dto.currency.CurrencyDto;
import pecunia_22.models.dto.making.MakingDto;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoteAdminDto extends CommonDto {
    private Long id;
    private Double denomination;
    private String series;
    private CurrencyDto currencies;
    private MakingDto makings;
    private Integer width;
    private Integer height;
    private String serialNumber;
}
