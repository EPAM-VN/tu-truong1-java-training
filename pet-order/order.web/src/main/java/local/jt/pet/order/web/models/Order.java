package local.jt.pet.order.web.models;

import jakarta.persistence.*;
import local.jt.pet.order.web.enums.OrderStatus;
import local.jt.pet.order.web.enums.PaymentMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public final class Order extends BaseEntity {
    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method",  nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "promotion_codes")
    private String promotionCodes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "order_has_promotions", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private Set<Promotion> promotions;
}
