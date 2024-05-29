package pecunia_22.services.customerService;

import org.springframework.stereotype.Service;
import pecunia_22.models.Customer;

import java.util.List;

@Service
public interface CustomerService {
    List<Customer> getAllCustomer();
    List<Customer> getAllCustomerOrderByID();
    Customer getCustomerById(Long id);
    Customer saveCustomer(Customer Customer);
    void deleteCustomerById(Long id);

    void updateCustomer(Customer Customer);

    Customer getCustomerByUUID(String customerUUID);}
