package pecunia_22.services.apiService;

import jakarta.ws.rs.client.Invocation;
import org.glassfish.jersey.client.ClientResponse;
import org.springframework.stereotype.Service;
import pecunia_22.models.others.*;

@Service
public interface ApiService {
    ClientResponse clientResponse(String url);
    GetApiMetal getApiMetal(String url);

    Invocation.Builder webResource(String url);
    GetRateCurrencyTableA getRateCurrencyTableA(String url, String[] codes);
    GetMetalSymbol getMetalSymbol(String url);
    GetMetalRate getMetalRate(String url, GetMetalSymbol getMetalSymbols);
}
