package br.com.orlands.manto.domain;

import br.com.orlands.manto.domain.enuns.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SHIPMENTS")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    public Shipment() {
    }

    public Shipment(Status status, String trackingNumber, Order order) {
        this.status = status;
        this.order = order;
    }

    public Long getId() {
        return shipmentId;
    }

    public void setId(Long id) {
        this.shipmentId = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + shipmentId +
                ", status=" + status +
                ", order=" + order +
                '}';
    }
}
