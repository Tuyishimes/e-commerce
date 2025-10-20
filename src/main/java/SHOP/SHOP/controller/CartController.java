package SHOP.SHOP.controller;

import SHOP.SHOP.model.Cart;
import SHOP.SHOP.service.CartService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/cart")

public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<Object>> getCart() {
        List<Cart> carts = cartService.getUserCart();

        List<Object> formattedCarts = Collections.singletonList(carts.stream().map(cart -> Map.of(
                "cart_id", cart.getId(),
                "total_items", cart.getItems().size(),
                "total_price", cart.getItems().stream()
                        .map(item -> item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                "items", cart.getItems().stream().map(item -> Map.of(
                        "product_id", item.getProduct().getId(),
                        "product_name", item.getProduct().getName(),
                        "quantity", item.getQuantity(),
                        "price", item.getProduct().getPrice(),
                        "subtotal", item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )).toList()
        )).toList());

        return ResponseEntity.ok(formattedCarts);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addItem(@RequestBody Map<String ,Object> request){
        Long productId = ((Number) request.get("productId")).longValue();
        int quantity = ((Number) request.get("quantity")).intValue();

        Cart cart = cartService.addItem(productId, quantity);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Item added to cart successfully");

        // Total price as BigDecimal
        response.put("cart", Map.of(
                "cart_id", cart.getId(),
                "total_items", cart.getItems().size(),
                "total_price", cart.getItems().stream()
                        .map(item -> item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                "items", cart.getItems().stream().map(item -> Map.of(
                        "product_id", item.getProduct().getId(),
                        "product_name", item.getProduct().getName(),
                        "quantity", item.getQuantity(),
                        "price", item.getProduct().getPrice(),
                        "subtotal", item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )).toList()
        ));

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{itemId}")
    public ResponseEntity<Map<String, Object>> updateQuantity(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> request) {

        if (!request.containsKey("quantity")) {
            throw new IllegalArgumentException("Missing 'quantity' in request body");
        }

        int quantity = ((Number) request.get("quantity")).intValue();
        Cart updatedCart = cartService.updateQuantity(itemId, quantity);

        Map<String, Object> response = Map.of(
                "status", "success",
                "message", "Cart item quantity updated successfully",
                "cart", Map.of(
                        "cart_id", updatedCart.getId(),
                        "total_items", updatedCart.getItems().size(),
                        "total_price", updatedCart.getTotalPrice(),
                        "items", updatedCart.getItems().stream().map(item -> Map.of(
                                "product_id", item.getProduct().getId(),
                                "product_name", item.getProduct().getName(),
                                "quantity", item.getQuantity(),
                                "price", item.getProduct().getPrice(),
                                "subtotal", item.getProduct().getPrice()
                                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                        )).toList()
                )
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Map<String, Object>> removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);

        Map<String, Object> response = Map.of(
                "status", "success",
                "message", "Item removed from cart"
        );

        return ResponseEntity.ok(response);
    }

}
