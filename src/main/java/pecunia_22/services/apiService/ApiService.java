package pecunia_22.services.apiService;

import org.glassfish.jersey.client.ClientResponse;
import org.springframework.stereotype.Service;
import pecunia_22.models.others.GetApiMetal;
import pecunia_22.models.others.GetRateCurrencyTableA;

@Service
public interface ApiService {
    ClientResponse clientResponse(String url);
    GetApiMetal getApiMetal(String url);

    GetRateCurrencyTableA getRateCurrencyTableA(String url, String[] codes);
}
