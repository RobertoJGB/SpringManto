package br.com.orlands.manto.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.orlands.manto.domain.enuns.Status;
import br.com.orlands.manto.service.ShipmentService;

@Controller
@RequestMapping("/shipments")
@PreAuthorize("hasRole('SELLER')")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping("/update-status")
    public String updateShipmentStatus(@RequestParam Long shipmentId,
                                       @RequestParam Status status) {
        shipmentService.updateStatus(shipmentId, status);
        return "redirect:/user/profile"; 
    }
}
