package pecunia_22.services.customerService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pecunia_22.models.Customer;
import pecunia_22.models.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomer() {
        return this.customerRepository.findAll();
    }

    @Override
    public List<Customer> getAllCustomerOrderByID() {
        return  this.customerRepository.getAllCustomerOrderById();
    }

    @Override
    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return customer.get();
        } else  {
            throw new RuntimeException("Customer Not Found For Id :: " + id);
        }
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return this.customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customerRepository.existsById(customer.getId())) {
            customerRepository.updateCustomer(customer.getName(), customer.getLastName(), customer.getCity(), customer.getZipCode(), customer.getStreet(), customer.getNumber(), customer.getEmail(), customer.getNick(), customer.getPhone(), customer.getDescription(), customer.getId());
        }else   {
            throw new RuntimeException("Podczas aktualizacji danych wystąpił bład (id - " + customer.getId() + " nie istnieje)");
        }
    }

    @Override
    public void deleteCustomerById(Long id) {
        if (customerRepository.existsById(id)) {
            this.customerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Record o id - " + id + " Który chcesz usuąć nie istnieje");
        }
    }

    @Override
    public Customer getCustomerByUUID(String customerUUID) {
        Customer customer = customerRepository.getCustomerByUUID(customerUUID);
        return customer;
    }
}
