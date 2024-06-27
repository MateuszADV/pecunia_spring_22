package pecunia_22.chartClass;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ReportMethod {
    private String name;
    private Integer quality;
    private List<Variable> variables = new ArrayList<>();


    public ReportMethod(String name, Integer quality) {
        this.name = name;
        this.quality = quality;
    }
}
