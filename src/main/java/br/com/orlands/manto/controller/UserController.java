package br.com.orlands.manto.controller;

import br.com.orlands.manto.domain.Order;
import br.com.orlands.manto.domain.OrderItem;
import br.com.orlands.manto.domain.UserDomain;
import br.com.orlands.manto.domain.enuns.UserRoles;
import br.com.orlands.manto.service.OrdemItemService;
import br.com.orlands.manto.service.OrderService;
import br.com.orlands.manto.service.UserService;

import java.util.ArrayList;
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

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final OrdemItemService orderItemService;

    public UserController(UserService userService, OrderService orderService, OrdemItemService orderItemService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('SELLER') or hasRole('BUYER')")
    public String userProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<UserDomain> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        UserDomain user = userOpt.get();
        model.addAttribute("user", user);

        if (UserRoles.ROLE_SELLER.equals(user.getUserRole())) {
            List<Order> sellerOrders = orderService.findBySeller(user);
            model.addAttribute("sellerOrders", sellerOrders);
        }

        if (UserRoles.ROLE_BUYER.equals(user.getUserRole())) {
            List<Order> buyerOrders = orderService.findByBuyer(user);
            List<OrderItem> buyerOrderItems = new ArrayList<>();

            for (Order order : buyerOrders) {
                List<OrderItem> items = orderItemService.findByOrderId(order.getOrderId());
                buyerOrderItems.addAll(items);
            }

            model.addAttribute("buyerOrders", buyerOrders);
            model.addAttribute("buyerOrderItems", buyerOrderItems);
        }

        return "user-profile";
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('SELLER') or hasRole('BUYER')")
    public String changePassword(
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<UserDomain> userOpt = userService.findByEmail(email);

        if (userOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Usuário não encontrado.");
            return "user-profile";
        }

        UserDomain user = userOpt.get();

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "As senhas não conferem.");
            model.addAttribute("user", user);
            return "user-profile";
        }

        // Chama o service para atualizar a senha
        userService.updatePassword(user, newPassword);

        model.addAttribute("successMessage", "Senha alterada com sucesso.");
        model.addAttribute("user", user);
        return "user-profile";
    }

    @PostMapping("/delete-account")
    @PreAuthorize("hasRole('SELLER') or hasRole('BUYER')")
    public String deleteAccount(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<UserDomain> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Usuário não encontrado.");
            return "user-profile";
        }

        UserDomain user = userOpt.get();

        userService.deleteUser(user);

        // Após deletar, desloga o usuário
        SecurityContextHolder.clearContext();

        // Redireciona para a página inicial ou página de logout
        return "redirect:/login?accountDeleted";
    }

}
