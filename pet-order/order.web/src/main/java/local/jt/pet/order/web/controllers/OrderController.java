package local.jt.pet.order.web.controllers;

import jakarta.validation.Valid;
import local.jt.pet.order.web.dto.CreateOrderCommand;
import local.jt.pet.order.web.dto.NewOrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v{version}/orders")
public class OrderController {
    @GetMapping(version = "1.0")
    public ResponseEntity<List<String>> getOrders() {
        return new ResponseEntity(Arrays.asList(new String[]{"1", "2", "3"}), HttpStatus.OK);
    }

    @PostMapping(version = "1.0")
    public ResponseEntity<NewOrderDto> createOrder(@Valid @RequestBody CreateOrderCommand cmd) {
        NewOrderDto resp = new NewOrderDto();
        resp.setOrderId(UUID.randomUUID().toString());

        return new ResponseEntity(resp,  HttpStatus.CREATED);
    }
}
