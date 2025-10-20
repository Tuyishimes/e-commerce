package SHOP.SHOP.controller;

import SHOP.SHOP.model.Order;
import SHOP.SHOP.repository.OrderRepository;
import SHOP.SHOP.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/orders")

public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> placeOrder() {
        Order order = orderService.placeOrder();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Order placed successfully",
                "order_id", order.getId(),
                "total_price", order.getTotalPrice(),
                "status_value", order.getStatus()
        ));
    }
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getUserOrder() {
        return ResponseEntity.ok(orderService.getUserOrder());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getUserOrderByid(id));
    }
    @PutMapping("update/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Order updatedOrder = orderService.updateStatus(id, status);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Order status updated",
                "order_id", updatedOrder.getId(),
                "new_status", updatedOrder.getStatus()
        ));
    }

}
