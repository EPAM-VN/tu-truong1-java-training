package local.jt.pet.order.web.models;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItem {
    private UUID id;
    private UUID orderId;
    private UUID petId;
    private Integer quantity;
    private Double price;
}
