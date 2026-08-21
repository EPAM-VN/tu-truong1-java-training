package local.jt.pet.order.application.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import local.jt.pet.order.domain.orders.aggregates.OrderItem;
import local.jt.pet.order.domain.orders.enums.OrderStatus;
import local.jt.pet.order.domain.orders.enums.PaymentMethod;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderCommand {
    @Valid

    @NotNull(message = "customerId is mandatory")
    private UUID customerId;

    @NotEmpty(message = "items is mandatory")
    private final List<OrderItem> items;

    @NotNull(message = "paymentMethod is mandatory")
    private PaymentMethod paymentMethod;
    private String notes;
}
