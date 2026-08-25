package local.jt.pet.order.web.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import local.jt.pet.order.web.enums.Membership;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "customers", indexes = {
        @Index(name = "idx_customers_email", columnList = "email",  unique = true),
})
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Customer extends Person {
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership", nullable = false)
    private Membership membership;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public boolean isMember() {
        return this.membership != Membership.GUEST;
    }
}
