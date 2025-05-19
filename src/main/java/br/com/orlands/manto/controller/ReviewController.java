package br.com.orlands.manto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.orlands.manto.domain.Product;
import br.com.orlands.manto.service.ProductService;
import br.com.orlands.manto.service.ReviewService;

@Controller 
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;


    public ReviewController(ReviewService reviewService, ProductService productService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

     @PostMapping("/create")
    public String saveReview(@RequestParam Long productId,
                             @RequestParam int rating,
                             @RequestParam String comment) {
        reviewService.saveReview(productId, rating, comment);

        // Redireciona para a página de detalhes do produto após salvar
        String slug = productService.findOptionalById(productId)
                .map(Product::getSlug)
                .orElse("erro");
        return "redirect:/products/" + slug;
    }

    

    


    
}
