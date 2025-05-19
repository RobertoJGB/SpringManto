package br.com.orlands.manto.service;

import org.springframework.stereotype.Service;

import br.com.orlands.manto.domain.Shipment;
import br.com.orlands.manto.domain.enuns.Status;
import br.com.orlands.manto.repositories.ShipmentRepository;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;   

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Shipment saveShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

      public void updateStatus(Long shipmentId, Status newStatus) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));
        shipment.setStatus(newStatus);
        shipmentRepository.save(shipment);
    }

}
