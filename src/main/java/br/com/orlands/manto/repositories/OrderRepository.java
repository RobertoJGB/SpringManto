package br.com.orlands.manto.repositories;

import org.springframework.stereotype.Repository;
import br.com.orlands.manto.domain.Order;
import br.com.orlands.manto.domain.UserDomain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyer(UserDomain buyer);
    List<Order> findBySeller(UserDomain seller);
}
