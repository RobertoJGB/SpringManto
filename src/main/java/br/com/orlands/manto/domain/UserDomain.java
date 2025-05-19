package br.com.orlands.manto.domain;

import java.util.List;

import br.com.orlands.manto.domain.enuns.UserRoles;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class UserDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoles userRole;

    // Usuário pode criar muitos produtos
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    // Pedidos onde o usuário é comprador
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> ordersAsBuyer;

    // Pedidos onde o usuário é vendedor
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> ordersAsSeller;

    // Usuário pode fazer muitas avaliações
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public UserDomain() {
    }

    public UserDomain(String name, String email, String password, UserRoles userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public Long getId() {
    return userId;
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoles getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Order> getOrdersAsBuyer() {
    return ordersAsBuyer;
}

public void setOrdersAsBuyer(List<Order> ordersAsBuyer) {
    this.ordersAsBuyer = ordersAsBuyer;
}

public List<Order> getOrdersAsSeller() {
    return ordersAsSeller;
}

public void setOrdersAsSeller(List<Order> ordersAsSeller) {
    this.ordersAsSeller = ordersAsSeller;
}

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "UserDomain [name=" + name + ", email=" + email + ", password=" + password
                + ", userRole=" + userRole + "]";
    }
}
