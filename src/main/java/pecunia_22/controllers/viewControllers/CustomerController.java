package pecunia_22.controllers.viewControllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pecunia_22.models.Customer;
import pecunia_22.models.dto.customer.CustomerDto;
import pecunia_22.models.dto.customer.CustomerDtoForm;
import pecunia_22.services.customerService.CustomerServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class CustomerController {
    private CustomerServiceImpl customerService;
    private Optional<Customer> customerTemp;

    @GetMapping("/customer")
    public String getIndex(ModelMap modelMap) {
        List<Customer> customers = customerService.getAllCustomerOrderByID();
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (Customer customer : customers) {
            customerDtos.add(new ModelMapper().map(customer, CustomerDto.class));
        }

        modelMap.addAttribute("customerDtos", customerDtos);

        System.out.println(JsonUtils.gsonPretty(customerDtos));
        return "customer/index";
    }

    @GetMapping("/customer/new")
    public String getNew(ModelMap modelMap) {
        CustomerDtoForm customerDtoForm = new CustomerDtoForm();
        customerDtoForm.setUniqueId(UUID.randomUUID().toString());
        customerDtoForm.setActive(true);
        modelMap.addAttribute("customerForm", customerDtoForm);
        return "customer/new";
    }

    @PostMapping("/customer/new")
    public String posNew(@ModelAttribute("customerForm")@Valid CustomerDtoForm customerDtoForm, BindingResult result, ModelMap modelMap) {
        System.out.println(JsonUtils.gsonPretty(customerDtoForm));

        if (result.hasErrors()) {
            return "customer/new";
        }
        Customer customer = new ModelMapper().map(customerDtoForm, Customer.class);
        customerService.saveCustomer(customer);
        return "redirect:/customer";
    }

    @GetMapping("/customer/edit/{customerId}")
    public String getEdit(@PathVariable Long customerId, ModelMap modelMap) {
        customerTemp = Optional.ofNullable(customerService.getCustomerById(customerId));
        CustomerDtoForm customerFormDto = new ModelMapper().map(customerTemp, CustomerDtoForm.class);
        modelMap.addAttribute("customerForm", customerFormDto);
        return "customer/edit";
    }

    @PostMapping("/customer/edit")
    public String postEdit(@ModelAttribute("customerForm")@Valid CustomerDtoForm customerDtoForm, BindingResult result, ModelMap modelMap) {
        System.out.println(JsonUtils.gsonPretty(customerDtoForm));
        if (result.hasErrors()) {
            return "customer/edit";
        }
        Customer customer= new ModelMapper().map(customerDtoForm, Customer.class);
//        bought.setId(boughtTemp.get().getId());
        try {
            customerService.updateCustomer(customer);
        }catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/customer";
    }

    @GetMapping("/customer/delete/{id}")
    public String getDelete(@PathVariable Long id, ModelMap modelMap) {
        try {
            customerService.deleteCustomerById(id);
            System.out.println("***************************************************");
            System.out.println("****KLIENT ZOZTAl USUNIETY*******************");
            System.out.println("***************************************************");
            return "redirect:/customer";
        }catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
