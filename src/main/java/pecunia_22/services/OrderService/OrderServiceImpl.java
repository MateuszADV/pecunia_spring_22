package pecunia_22.services.OrderService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pecunia_22.models.Order;
import pecunia_22.models.repositories.OrderRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrder() {
        return this.orderRepository.findAll();
    }

    @Override
    public void saveOrder(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    public Order saveOrderGet(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public Order getOrderFindById(Long id) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Order Not Found For Id :: " + id);
        }
    }

    @Override
    public void deleteOrderById(Long id) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Element O podamyn id - " + id + " ,który chcesz usunąć nie istnieje");
        }
    }

    @Override
    public List<Order> getOrderBycustomer(String customerUUID) {
        List<Order> orders = orderRepository.getOrderbyCustomerUUID(customerUUID);
        return orders;
    }

    @Override
    public Order getOrderByNumberOrder(String numberOrder) {
        Optional<Order> optional = orderRepository.getOrderByNumberOrder(numberOrder);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Zamówienie o podanym numerze: " + numberOrder + " nie istnieje");
        }
    }

    @Override
    public String getLastNumberOrder() {
        Order order = orderRepository.getLastOrder();
        System.out.println("++++++++++++++ZAMOWIENIE START+++++++++++++++++++++++++++++++");
        if (order == null) {
            return "";
        }
        String lastNumberOrder = order.getNumberOrder();
        System.out.println(lastNumberOrder);
        System.out.println("++++++++++++++ZAMOWIENIE STOP+++++++++++++++++++++++++++++++");

        return lastNumberOrder;
    }

    @Override
    public Boolean checkLastNumberOrder(String lastNumberOrder) {
        Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/\\d{3,5}/\\d{3,5}");
        return pattern.matcher(lastNumberOrder).matches();
    }

    @Override
    public String returnFirstNumberOrder() {
        String firstNumberOrder = getDateOrder() + "/0001/0001";
        return firstNumberOrder;
    }

    @Override
    public String getDateOrder() {
        LocalDate localDate = LocalDate.now();
        Integer year = localDate.getYear();
        Integer month = localDate.getMonthValue();

        String dateOrder = year.toString() + '/' + month.toString();
        return dateOrder;
    }

    @Override
    public String getNextNumber(String number) {
        Integer lenght;
        Integer nextNumber = Integer.valueOf(number) + 1;
        String nextNumberStr = nextNumber.toString();
        lenght = number.length() - nextNumberStr.length();
        for (int i = 0; i <= lenght-1; i++  ) {
            nextNumberStr = "0" + nextNumberStr;
        }
        return nextNumberStr;
    }

    @Override
    public Boolean checkYearOrder(String lastNumberOrder) {
        LocalDate localDate = LocalDate.now();
        Integer year = localDate.getYear();
        String NumberOrder = lastNumberOrder;
        String[] elementsOrder = lastNumberOrder.split("/");

//        System.out.println(elementsOrder[0].equals(year.toString()));
        return elementsOrder[0].equals(year.toString());
    }

    @Override
    public String getNextNumberOrder(String lastNumberOrder) {
        if (checkLastNumberOrder(lastNumberOrder)) {
            LocalDate localDate = LocalDate.now();
            Integer year = localDate.getYear();
            Integer month = localDate.getMonthValue();
            String nextNumberOrder = "";
            String NumberOrder = lastNumberOrder;
            String[] elementsOrder = lastNumberOrder.split("/");
            if (checkYearOrder(lastNumberOrder)) {
                nextNumberOrder = year + "/" + month.toString() + "/" + getNextNumber(elementsOrder[2]) + "/" + getNextNumber(elementsOrder[3]);
            } else {
                nextNumberOrder = year + "/"  + month.toString() + "/" + getNextNumber(elementsOrder[2]) + "/0001";
            }
            return nextNumberOrder;
        } else {
            return returnFirstNumberOrder();
        }
    }
}
