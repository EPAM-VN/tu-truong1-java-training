package local.jt.pet.order.web.controllers;

import jakarta.validation.Valid;
import local.jt.pet.order.web.features.orders.viewmodels.NewOrderModel;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v{version}/orders")
public class OrderController {
    @GetMapping(version = "1.0")
    public List<String> getOrders() {
        return Arrays.asList(new String[] {"1", "2", "3"});
    }

    @PostMapping(version = "1.0")
    public NewOrderModel createOrder(@Valid @RequestBody NewOrderModel newOrderDto) {
        return newOrderDto;
    }
}
