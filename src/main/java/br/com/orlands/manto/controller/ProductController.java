package br.com.orlands.manto.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.orlands.manto.domain.Product;
import br.com.orlands.manto.domain.UserDomain;
import br.com.orlands.manto.service.ProductService;
import br.com.orlands.manto.service.UserService;
import br.com.orlands.manto.utils.SlugGenerator;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/new-product")
    @PreAuthorize("hasRole('SELLER')")
    public String newProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "new-product";
    }

    @PostMapping("/create")
    public String newProduct(@Valid @ModelAttribute("product") Product product,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model,
            Principal principal) throws IOException {

        if (result.hasErrors()) {
            return "new-product";
        }

        // Salva imagem
        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "-" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get("src/main/resources/static/img");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            product.setImageUrl("/img/" + fileName);
        }

        // Buscar usuário autenticado e associar ao produto
        String email = principal.getName(); // pega o email do usuário logado
        UserDomain user = userService.findByEmail(email).orElseThrow();
        product.setUser(user); // seta o dono do produto

        // Gera o slug
        product.setSlug(SlugGenerator.toSlugRandomCode(product.getNameProduct()));

        productService.saveProduct(product);

        return "redirect:/products/new-product";
    }

    @GetMapping("/{slug}")
    public String getProduct(@PathVariable("slug") String slug, Model model) {
        Optional<Product> optionalProduct = productService.findBySlug(slug);

        if (optionalProduct.isEmpty()) {
            model.addAttribute("errorMessage", "Produto não encontrado.");
            return "product-details";
        }

        Product product = optionalProduct.get();

        // Adiciona o produto e as reviews no model
        model.addAttribute("product", product);
        model.addAttribute("reviews", product.getReviews());

        return "product-details";
    }

    // metodo para listar produto especifico, apenas o vendedor pode acessar,
    // verifica se o vendedor é o dono do produto
    @GetMapping("/myproduct/{slug}")
    public String showMyProduct(@PathVariable String slug, Principal principal, Model model) {
        String userEmail = principal.getName(); // email do usuário logado
        UserDomain user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Product> productOptional = productService.findBySlug(slug);

        if (productOptional.isEmpty() || !productOptional.get().getUser().getEmail().equals(user.getEmail())) {
            // Produto não encontrado ou não pertence ao usuário logado
            return "redirect:/user/profile";
        }

        Product product = productOptional.get();
        model.addAttribute("product", product);
        return "myproduct-details";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile,
            Principal principal) throws IOException {

        Optional<UserDomain> optionalUser = userService.findByEmail(principal.getName());
        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }
        UserDomain user = optionalUser.get();
        product.setUser(user);

        if (imageFile != null && !imageFile.isEmpty()) {
            // Salvar a nova imagem e atualizar o imageUrl
            String uploadDir = "src/main/resources/static/img"; // ajuste o caminho
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, imageFile.getBytes());

            product.setImageUrl("/img/" + filename); // caminho para acesso público
        } else {
            // Nenhuma imagem nova foi enviada => manter a URL antiga do formulário
            // Aqui o product.imageUrl já vem preenchido pelo formulário, então só mantemos
            // Se quiser, pode garantir que a URL antiga não seja nula, por exemplo:
            if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
                // Busca a URL antiga no banco para não perder
                Optional<Product> existingProduct = productService.findBySlug(product.getSlug());
                if (existingProduct.isPresent()) {
                    product.setImageUrl(existingProduct.get().getImageUrl());
                }
            }
        }

        productService.updateProduct(product);

        return "redirect:/user/profile";
    }

    @GetMapping("/delete/{slug}")
    public String deleteProduct(@PathVariable String slug, Principal principal) {
        Optional<Product> productOptional = productService.findBySlug(slug);
        Optional<UserDomain> userOptional = userService.findByEmail(principal.getName());

        if (userOptional.isEmpty()) {
            return "redirect:/login?error"; // ou outra página de erro
        }

        UserDomain user = userOptional.get();

        if (productOptional.isPresent() && productOptional.get().getUser().getEmail().equals(user.getEmail())) {
            productService.deleteProductById(productOptional.get().getId());
        }

        return "redirect:/user/profile";
    }

}
