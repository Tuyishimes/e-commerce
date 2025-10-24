package SHOP.SHOP.service;

import SHOP.SHOP.model.Order;
import SHOP.SHOP.model.Payment;
import SHOP.SHOP.model.User;
import SHOP.SHOP.repository.CartRepository;
import SHOP.SHOP.repository.OrderRepository;
import SHOP.SHOP.repository.PaymentRepository;
import SHOP.SHOP.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.rmi.server.UID;
import java.sql.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserRepository userRepository;
  private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final Random random = new Random();
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
    public Payment checkout(Long OrderId){
        Order order=orderRepository.findById(OrderId)
                .orElseThrow(()->new RuntimeException("No order Found"));
        User user=order.getUser();
        if (order.getStatus() != Order.Status.PENDING) {
            throw new RuntimeException("Only pending orders can be paid.");
        }
        Payment payment=new Payment();
        payment.setOrder(order);
        payment.setUser(user);
        payment.setAmount(order.getTotalPrice());
        payment.setReference("Pay_"+UUID.randomUUID().toString().substring(0,8));
        payment.setStatus("PENDING");
        payment=paymentRepository.save(payment);
        String result = simulateGatewayProcess();
        if(result.equals("APPROVED")){
            payment.setStatus("SUCCESS");
            order.setStatus(Order.Status.PAID);

        }
        else{
            payment.setStatus("FAILED");
            order.setStatus(Order.Status.CANCELLED);
            orderRepository.save(order);
        }
        orderRepository.save(order);
        return paymentRepository.save(payment);
    }
    private String simulateGatewayProcess() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return random.nextBoolean() ? "APPROVED" : "DECLINED";
    }
    public Payment getPaymentById(Long Id){
        return paymentRepository.findById(Id)
                .orElseThrow(()->new RuntimeException("Not Found"));
    }
    public List<Payment> getAllPaymentDetails(){
        return paymentRepository.findAll()
                ;

    }
}
