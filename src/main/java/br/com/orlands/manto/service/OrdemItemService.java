package br.com.orlands.manto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.orlands.manto.domain.OrderItem;

import br.com.orlands.manto.repositories.OrderItemRepository;

@Service
public class OrdemItemService {
    private final OrderItemRepository orderItemRepository;  

    public OrdemItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }
    
    public List<OrderItem> findByOrderId(Long orderId) {
    return orderItemRepository.findByOrderOrderId(orderId);
}
}
