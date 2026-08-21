package local.jt.pet.order.domain.orders.aggregates;

import local.jt.pet.order.domain.orders.enums.OrderStatus;
import local.jt.pet.order.domain.orders.enums.PaymentMethod;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Order {
    private final UUID customerId;
    private final List<OrderItem> items;
    private String notes;
    private OrderStatus status = OrderStatus.DRAFT;
    private PaymentMethod paymentMethod;
}
