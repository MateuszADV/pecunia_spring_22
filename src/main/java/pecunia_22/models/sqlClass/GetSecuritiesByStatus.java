package pecunia_22.models.sqlClass;

import lombok.*;
import pecunia_22.models.dto.making.MakingDto;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetSecuritiesByStatus extends GetByStatus{
    private Long securityId;
    private MakingDto makings;
    private Integer width;
    private Integer height;
}
