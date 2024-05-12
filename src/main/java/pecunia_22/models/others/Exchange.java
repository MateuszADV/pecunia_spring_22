package pecunia_22.models.others;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Exchange {
    private String table;
    private String no;
    private String effectiveDate;
    private List<Rate> rates;
}
