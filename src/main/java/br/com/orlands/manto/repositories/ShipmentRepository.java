package br.com.orlands.manto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.orlands.manto.domain.Shipment;


@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    
    
}
