package local.jt.pet.order.domain.orders.aggregates;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItem {
    private UUID petId;
    private Integer quantity;
    private Double price;
}
