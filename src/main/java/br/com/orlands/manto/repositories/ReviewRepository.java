package br.com.orlands.manto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import br.com.orlands.manto.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByProductId(Long productId);
}
