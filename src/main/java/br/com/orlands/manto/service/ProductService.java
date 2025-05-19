package br.com.orlands.manto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.orlands.manto.domain.Product;
import br.com.orlands.manto.repositories.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    

    public Optional<Product> findOptionalById(Long productId) {
    return productRepository.findById(productId);
}

public Optional<Product> findBySlug(String slug){
    return productRepository.findBySlug(slug);
}

//  Atualizar produto
    public Optional<Product> updateProduct(Product updatedProduct) {
        return productRepository.findBySlug(updatedProduct.getSlug()).map(existingProduct -> {
            existingProduct.setNameProduct(updatedProduct.getNameProduct());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setQuantity(updatedProduct.getQuantity());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());
            return productRepository.save(existingProduct);
        });
    }

    // ✅ Deletar produto
    public boolean deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
