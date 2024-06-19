package pecunia_22.controllers.viewControllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pecunia_22.models.*;
import pecunia_22.models.dto.coin.CoinDto;
import pecunia_22.models.dto.note.NoteDto;
import pecunia_22.models.dto.order.OrderDto;
import pecunia_22.models.dto.orderItem.OrderItemDto;
import pecunia_22.models.dto.orderItem.OrderItemDtoForm;
import pecunia_22.models.dto.security.SecurityDto;
import pecunia_22.models.rersponse.orderResponse.OrderItemResp;
import pecunia_22.models.sqlClass.GetCoinsByStatus;
import pecunia_22.models.sqlClass.GetNotesByStatus;
import pecunia_22.models.sqlClass.GetSecuritiesByStatus;
import pecunia_22.services.OrderService.OrderServiceImpl;
import pecunia_22.services.coinService.CoinServiceImpl;
import pecunia_22.services.noteServices.NoteServiceImpl;
import pecunia_22.services.orderItemService.OrderItemServiceImpl;
import pecunia_22.services.securityService.SecurityServiceImpl;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderItemController {

    private OrderItemServiceImpl orderItemService;
    private OrderServiceImpl orderService;
    private NoteServiceImpl noteService;
    private CoinServiceImpl coinService;
    private SecurityServiceImpl securityService;

    @Autowired
    public OrderItemController(OrderItemServiceImpl orderItemService, OrderServiceImpl orderService, NoteServiceImpl noteService, CoinServiceImpl coinService, SecurityServiceImpl securityService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.noteService = noteService;
        this.coinService = coinService;
        this.securityService = securityService;
    }

    @GetMapping("/orderItem/{orderId}")
    public String getIndex(@PathVariable Long orderId, ModelMap modelMap) {
        System.out.println(orderId);
        OrderItemResp orderItemResp = new OrderItemResp();
        Order order = orderService.getOrderFindById(orderId);
        OrderDto orderDto = new ModelMapper().map(order, OrderDto.class);

        List<OrderItem> orderItems = new ArrayList<>();

        try {
            orderItems = orderItemService.getOrderItemByOrderId(orderId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDtos.add(new ModelMapper().map(orderItem, OrderItemDto.class));
        }
        orderItemResp.setOrders(orderDto);
        orderItemResp.setOrderItems(orderItemDtos);

        Double sum = orderItemDtos.stream()
                .mapToDouble(i -> i.getFinalPrice() * i.getQuantity())
                .sum();
        System.out.println("############################ START ##################################");
        System.out.println(sum);
        System.out.println("############################ STOP ##################################");
        modelMap.addAttribute("itemTotalSum", sum);
        modelMap.addAttribute("orderItem", orderItemResp);

        return "orderItem/index";
    }

    @GetMapping("/orderItem/forSell/")
    public String getItemForSell(@RequestParam("orderId") Long orderId, ModelMap modelMap) {
        System.out.println("444444444444444444444444444444444444444444444444");
        System.out.println(orderId);
        System.out.println("444444444444444444444444444444444444444444444444");
        List<GetNotesByStatus> getNotesByStatusList = noteService.getNoteByStatus("FOR SELL");
        List<GetCoinsByStatus> getCoinsByStatusList = coinService.getCoinsByStatus("FOR SELL");
        List<GetSecuritiesByStatus> getSecuritiesByStatusList = securityService.getSecurityByStatus("FOR SELL");

//        System.out.println(JsonUtils.gsonPretty(getCoinsByStatusList));
//        System.out.println(JsonUtils.gsonPretty(getNotesByStatusList.get(1)));
//        System.out.println(JsonUtils.gsonPretty(getCoinsByStatusList.get(1)));
//        System.out.println(JsonUtils.gsonPretty(getSecuritiesByStatusList.get(1)));

        modelMap.addAttribute("orderId", orderId);

        modelMap.addAttribute("notesForSell", getNotesByStatusList);
        modelMap.addAttribute("coinsForSell", getCoinsByStatusList);
        modelMap.addAttribute("securitiesForSell", getSecuritiesByStatusList);

        return "orderItem/itemForSell";
    }

    @GetMapping("/orderItem/new/")
    public String getNew(@RequestParam("orderId") Long orderid,
                         @RequestParam("itemId") Long itemId,
                         @RequestParam("pattern") String pattern,
                         ModelMap modelMap) {

        System.out.println("77777777777777777777777777777777777777777");
        System.out.println(orderid);
        System.out.println(itemId);
        System.out.println(pattern);
        System.out.println("77777777777777777777777777777777777777777");

        Order order = orderService.getOrderFindById(orderid);
        OrderDto orderDto = new ModelMapper().map(order, OrderDto.class);

        OrderItemDtoForm orderItemDtoForm = new OrderItemDtoForm();
        orderItemDtoForm.setOrders(orderDto);
        orderItemDtoForm.setPattern(pattern);
        orderItemDtoForm.setQuantity(1);

        if (pattern.equals("NOTE")) {
            Note note = noteService.getNoteById(itemId);
            NoteDto noteDto = new ModelMapper().map(note, NoteDto.class);
            orderItemDtoForm.setCountries(noteDto.getCurrencies().getCountries());
            orderItemDtoForm.setNotes(noteDto);
            orderItemDtoForm.setUnitQuantity(noteDto.getUnitQuantity());
            orderItemDtoForm.setFinalPrice(noteDto.getPriceSell());

            modelMap.addAttribute("orderItemForm", orderItemDtoForm);
        } else if (pattern.equals("COIN")) {
            Coin coin = coinService.getCoinById(itemId);
            CoinDto coinDto = new ModelMapper().map(coin, CoinDto.class);
            orderItemDtoForm.setCountries(coinDto.getCurrencies().getCountries());
            orderItemDtoForm.setCoins(coinDto);
            orderItemDtoForm.setUnitQuantity(coinDto.getUnitQuantity());
            orderItemDtoForm.setFinalPrice(coinDto.getPriceSell());

            System.out.println("5555555555555555555555555555555555555555555555555555555555");
            System.out.println(JsonUtils.gsonPretty(orderItemDtoForm));
            System.out.println("5555555555555555555555555555555555555555555555555555555555");

            modelMap.addAttribute("orderItemForm", orderItemDtoForm);
        } else if (pattern.equals("SECURITY")) {
            Security security = securityService.getSecurityById(itemId);
            SecurityDto securityDto = new ModelMapper().map(security, SecurityDto.class);
            orderItemDtoForm.setCountries(securityDto.getCurrencies().getCountries());
            orderItemDtoForm.setSecurities(securityDto);
            orderItemDtoForm.setUnitQuantity(securityDto.getUnitQuantity());
            orderItemDtoForm.setFinalPrice(securityDto.getPriceSell());

            modelMap.addAttribute("orderItemForm", orderItemDtoForm);
        }
        return "orderItem/new";
    }

    @PostMapping("/orderItem/new")
    public String postNew(@ModelAttribute("orderItemForm")@Valid OrderItemDtoForm orderItemDtoForm, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            if (orderItemDtoForm.getPattern().equals("NOTE")) {
                Note note = noteService.getNoteById(orderItemDtoForm.getNotes().getId());
                NoteDto noteDto = new ModelMapper().map(note, NoteDto.class);
                orderItemDtoForm.setNotes(noteDto);
            } else if (orderItemDtoForm.getPattern().equals("COIN")) {
                Coin coin = coinService.getCoinById(orderItemDtoForm.getCoins().getId());
                CoinDto coinDto = new ModelMapper().map(coin, CoinDto.class);
                orderItemDtoForm.setCoins(coinDto);
            } else  if (orderItemDtoForm.getPattern().equals("SECURITY")) {
                Security security = securityService.getSecurityById(orderItemDtoForm.getSecurities().getId());
                SecurityDto securityDto = new ModelMapper().map(security, SecurityDto.class);
                orderItemDtoForm.setSecurities(securityDto);
            }
            return "orderItem/new";
        }
        OrderItem orderItem = new ModelMapper().map(orderItemDtoForm, OrderItem.class);
        System.out.println(JsonUtils.gsonPretty(orderItem));
        try {
            orderItemService.saveOrderItem(orderItem);
            System.out.println("Dodanie elementu do bazy");
        } catch (Exception e) {
            System.out.println("Jakiś błąd!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(e.getMessage());
        }
        return "redirect:/orderItem/" + orderItemDtoForm.getOrders().getId();
    }

    @GetMapping("/orderItem/edit/")
    public String Edit(@RequestParam("itemId") Long itemId,
                       ModelMap modelMap) {
        OrderItem orderItem = orderItemService.getOrderItemFindById(itemId);
        OrderItemDtoForm orderItemDtoForm = new ModelMapper().map(orderItem, OrderItemDtoForm.class);

        System.out.println("111111111111111111 -START- 11111111111111111111111111111111");
        System.out.println(itemId);
        System.out.println(JsonUtils.gsonPretty(orderItemDtoForm));
        System.out.println("111111111111111111 -STOP- 11111111111111111111111111111111");

        modelMap.addAttribute("orderItemForm", orderItemDtoForm);
        return "orderItem/edit";
    }

    @PostMapping("/orderItem/edit")
    public String postEdit(@ModelAttribute("orderItemForm")@Valid OrderItemDtoForm orderItemDtoForm, BindingResult result, ModelMap modelMap) {

        if (result.hasErrors()) {
            if (orderItemDtoForm.getPattern().equals("NOTE")) {
                Note note = noteService.getNoteById(orderItemDtoForm.getNotes().getId());
                NoteDto noteDto = new ModelMapper().map(note, NoteDto.class);
                orderItemDtoForm.setNotes(noteDto);
            } else if (orderItemDtoForm.getPattern().equals("COIN")) {
                Coin coin = coinService.getCoinById(orderItemDtoForm.getCoins().getId());
                CoinDto coinDto = new ModelMapper().map(coin, CoinDto.class);
                orderItemDtoForm.setCoins(coinDto);
            } else  if (orderItemDtoForm.getPattern().equals("SECURITY")) {
                Security security = securityService.getSecurityById(orderItemDtoForm.getSecurities().getId());
                SecurityDto securityDto = new ModelMapper().map(security, SecurityDto.class);
                orderItemDtoForm.setSecurities(securityDto);
            }
            System.out.println(result.toString());
            return "orderItem/edit";
        }

        OrderItem orderItem = new ModelMapper().map(orderItemDtoForm, OrderItem.class);
        System.out.println(JsonUtils.gsonPretty(orderItem));
        try {
            orderItemService.saveOrderItem(orderItem);
            System.out.println("Dodanie elementu do bazy");
        } catch (Exception e) {
            System.out.println("Jakiś błąd!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(e.getMessage());
        }
        return "redirect:/orderItem/" + orderItemDtoForm.getOrders().getId();
    }

    @GetMapping("/orderItem/delete/{orderItemId}")
    public String delete(@PathVariable Long orderItemId, ModelMap modelMap) {
        OrderItem orderItem = orderItemService.getOrderItemFindById(orderItemId);
        OrderItemDto orderItemDto = new ModelMapper().map(orderItem, OrderItemDto.class);
        orderItemService.deleteOrderItemById(orderItemId);
        return "redirect:/orderItem/" + orderItemDto.getOrders().getId();
    }
}
