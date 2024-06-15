package pecunia_22.models.sqlClass;

import lombok.*;
import pecunia_22.models.dto.making.MakingDto;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetNotesByStatus extends GetByStatus{
    private Long noteId;
    private MakingDto makings;
    private Integer width;
    private Integer height;
}
