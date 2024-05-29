package pecunia_22.models.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pecunia_22.models.Customer;

import java.util.List;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "SELECT cus FROM Customer cus ORDER BY cus.id")
    List<Customer> getAllCustomerOrderById();

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE customers SET name = :name, last_name = :lastName, city = :city, zip_code = :zipCode, street = :street, number = :number, email = :email, " +
                    "nick = :nick, phone = :phone, description = :description " +
                    "WHERE id = :id")
    Integer updateCustomer(@Param("name") String name,
                           @Param("lastName") String lastName,
                           @Param("city") String city,
                           @Param("zipCode") String zipCode,
                           @Param("street") String street,
                           @Param("number") String number,
                           @Param("email") String email,
                           @Param("nick") String nick,
                           @Param("phone") String phone,
                           @Param("description") String description,
                           @Param("id") Long id);

    @Query(value = "SELECT cus FROM Customer cus " +
            " WHERE cus.uniqueId = ?1 ")
    Customer getCustomerByUUID(String UUID);
}
