package br.com.orlands.manto.controller;

import java.util.List;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.orlands.manto.domain.Product;
import br.com.orlands.manto.service.ProductService;

@Controller
public class FlowController {

    private final ProductService productService;

    public FlowController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SELLER"))) {
            return "redirect:/user/profile"; // redireciona para outro controller
        }

        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("products", allProducts);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
