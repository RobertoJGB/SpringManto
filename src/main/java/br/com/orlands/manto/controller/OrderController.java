package br.com.orlands.manto.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.orlands.manto.domain.Order;
import br.com.orlands.manto.domain.OrderItem;
import br.com.orlands.manto.domain.Product;
import br.com.orlands.manto.domain.Shipment;
import br.com.orlands.manto.domain.UserDomain;
import br.com.orlands.manto.domain.enuns.Status;
import br.com.orlands.manto.service.OrderService;
import br.com.orlands.manto.service.ProductService;
import br.com.orlands.manto.service.ShipmentService;
import br.com.orlands.manto.service.UserService;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final ShipmentService shipmentService;

    public OrderController(OrderService orderService, ProductService productService, UserService userService, ShipmentService shipmentService) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
        this.shipmentService = shipmentService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public String createOrder(@RequestParam Long productId,
            @RequestParam int quantity,
            Principal principal) {

        Product product = productService.findOptionalById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Quantidade solicitada maior que disponível.");
        }

        String email = principal.getName();
        UserDomain buyer = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UserDomain seller = product.getUser();
        double total = product.getPrice() * quantity;

        Order order = new Order(buyer, seller, total);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());

        order.setOrderItems(List.of(item));

        product.setQuantity(product.getQuantity() - quantity);
        productService.saveProduct(product);

        orderService.saveOrder(order); // salva Order e OrderItem

        // Cria Shipment com status PENDENTE 
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setStatus(Status.PENDENTE); 
        

        shipmentService.saveShipment(shipment);

        return "redirect:/orders/my-orders";
    }
    

    @GetMapping("/my-orders")
    public String listUserOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // email do usuário logado

        Optional<UserDomain> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        UserDomain user = userOpt.get();

        // buscando pelo buyer
        List<Order> orders = orderService.findByBuyer(user);
        model.addAttribute("orders", orders);

        return "my-orders";
    }

    

    

}
