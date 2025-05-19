package br.com.orlands.manto.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserDomain buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserDomain seller;

    @Column(nullable = false)
    private double total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Shipment shipment;

    public Order() {
    }

    public Order(Long orderId, UserDomain buyer, UserDomain seller, double total) {
        this.orderId = orderId;
        this.buyer = buyer;
        this.seller = seller;

        this.total = total;
    }

    public Order(UserDomain buyer, UserDomain seller, double total) {
        this.buyer = buyer;
        this.seller = seller;

        this.total = total;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public UserDomain getBuyer() {
        return buyer;
    }

    public void setBuyer(UserDomain buyer) {
        this.buyer = buyer;
    }

    public UserDomain getSeller() {
        return seller;
    }

    public void setSeller(UserDomain seller) {
        this.seller = seller;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", buyer=" + buyer + ", seller=" + seller + ", total=" + total + "]";
    }
}
