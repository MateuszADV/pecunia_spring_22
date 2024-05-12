package pecunia_22.models.others;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

public class Rate {
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("cod")
    private String cod;
    @JsonProperty("mid")
    private Double mid;
}
