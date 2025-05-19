package br.com.orlands.manto.repositories;

import org.springframework.stereotype.Repository;
import br.com.orlands.manto.domain.OrderItem;
import br.com.orlands.manto.domain.UserDomain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderOrderId(Long orderId);

    
}
