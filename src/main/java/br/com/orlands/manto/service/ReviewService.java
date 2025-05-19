package br.com.orlands.manto.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.orlands.manto.domain.Product;
import br.com.orlands.manto.domain.Review;
import br.com.orlands.manto.domain.UserDomain;
import br.com.orlands.manto.repositories.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository, ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

       public void saveReview(Long productId, int rating, String comment) {
        // Busca o produto
        Product product = productService.findOptionalById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Recupera o usuário autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDomain user = userService.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Cria e salva a review
        Review review = new Review(rating, comment, product, user);
        reviewRepository.save(review);
    }
    
}
