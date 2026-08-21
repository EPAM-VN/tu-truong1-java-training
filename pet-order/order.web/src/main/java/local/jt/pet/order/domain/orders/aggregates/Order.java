package local.jt.pet.order.domain.orders.aggregates;

import jakarta.persistence.*;
import local.jt.pet.order.domain.orders.enums.OrderStatus;
import local.jt.pet.order.domain.orders.enums.PaymentMethod;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public final class Order {
    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method",  nullable = false)
    private PaymentMethod paymentMethod;
}
