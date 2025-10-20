package SHOP.SHOP.service;


import SHOP.SHOP.model.*;
import SHOP.SHOP.repository.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
    public Order placeOrder(){
        User user=getCurrentUser();
        Cart cart=cartRepository.findByUser(user)
                .orElseThrow(()-> new RuntimeException("Cart not found"));

        if(cart.getItems().isEmpty()){
            throw new RuntimeException("Cart Is empty");
        }
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order=new Order();
        order.setUser(user);
        order.setTotalPrice(total);
        order.setStatus(Order.Status.valueOf("PENDING"));
        order = orderRepository.save(order);
        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(item.getProduct().getPrice().doubleValue()));
            orderItemRepository.save(orderItem);


        }
        cartItemRepository.deleteAll(cart.getItems());

        return order;
    }
    public List<Map<String, Object>> getUserOrder() {
        User user = getCurrentUser();
        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(order -> {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", order.getId());

            List<Map<String, Object>> items = order.getOrderItems().stream().map(item -> {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("productName", item.getProduct().getName());
                itemData.put("price", item.getProduct().getPrice());
                itemData.put("quantity", item.getQuantity());
                return itemData;
            }).toList();

            orderData.put("items", items);

            BigDecimal total = items.stream()
                    .map(i -> ((BigDecimal) i.get("price")).multiply(BigDecimal.valueOf((Integer) i.get("quantity"))))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            orderData.put("total", total);
            return orderData;
        }).toList();
    }

    public Order getOrder(Long Id){
        return orderRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Order is now found"));

    }
    public Order updateStatus(Long id, String status) {
        Order order = getOrder(id);
        order.setStatus(Order.Status.valueOf(status));
        return orderRepository.save(order);
    }
}
