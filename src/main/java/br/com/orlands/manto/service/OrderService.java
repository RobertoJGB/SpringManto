package br.com.orlands.manto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.orlands.manto.domain.Order;
import br.com.orlands.manto.domain.UserDomain;
import br.com.orlands.manto.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
   

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> findByBuyer(UserDomain buyer) {
        return orderRepository.findByBuyer(buyer);
    }

    public List<Order> findBySeller(UserDomain seller) {
        return orderRepository.findBySeller(seller);
    }

    

}
