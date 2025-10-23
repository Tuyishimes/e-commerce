package SHOP.SHOP.controller;

import SHOP.SHOP.model.Payment;
import SHOP.SHOP.repository.PaymentRepository;
import SHOP.SHOP.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<Map<String, Object>> checkout(@RequestParam Long orderId){
        Payment payment= paymentService.checkout(orderId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("payment_id", payment.getId());
        response.put("order_id", payment.getOrder().getId());
        response.put("user_email", payment.getUser().getEmail());
        response.put("amount", payment.getAmount());
        response.put("status", payment.getStatus());
        response.put("reference", payment.getReference());
        response.put("created_at", payment.getCreatedAt());

        return ResponseEntity.ok(response);

    }
    @GetMapping("/status/{id}")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(@PathVariable Long Id){
        Payment payment = paymentService.getPaymentById(Id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("payment_id", payment.getId());
        response.put("order_id", payment.getOrder().getId());
        response.put("amount", payment.getAmount());
        response.put("status", payment.getStatus());
        response.put("reference", payment.getReference());
        response.put("created_at", payment.getCreatedAt());

        return ResponseEntity.ok(response);
    }
    @GetMapping("/admin/all")
    public ResponseEntity<List<Map<String, Object>>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPaymentDetails();

        List<Map<String, Object>> response = payments.stream().map(payment -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("payment_id", payment.getId());
            map.put("order_id", payment.getOrder() != null ? payment.getOrder().getId() : null);
            map.put("user_email", payment.getUser() != null ? payment.getUser().getEmail() : null);
            map.put("amount", payment.getAmount());
            map.put("status", payment.getStatus());
            map.put("reference", payment.getReference());
            map.put("created_at", payment.getCreatedAt());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
