package pecunia_22.models.others;

import jakarta.ws.rs.core.Response;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiResponseInfo {
    private Date date;
    private Response.StatusType responseStatusInfo;
}
