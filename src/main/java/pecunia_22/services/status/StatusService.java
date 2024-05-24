package pecunia_22.services.status;

import org.springframework.stereotype.Service;
import pecunia_22.models.Status;

import java.util.List;

@Service
public interface StatusService {
    List<Status> getAllStatuses();
    void saveStatus(Status status);
    Status getStatusById(Long id);
    void deleteStatusById(Long id);
}
