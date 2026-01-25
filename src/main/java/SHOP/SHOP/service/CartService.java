package SHOP.SHOP.service;

import SHOP.SHOP.model.*;
import SHOP.SHOP.repository.*;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }


    public Cart addItem(Long productId, int quantity) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);

        return cartRepository.findByUser(user).get();
    }

    public List<CartItem> getCartItems() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user).orElseThrow();
        return cart.getItems();
    }

    public Cart updateQuantity(Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return item.getCart();
    }

//    public void removeItem(Long itemId) {
//        CartItem item = cartItemRepository.findById(itemId)
//                .orElseThrow(() -> new RuntimeException("Cart item not found"));
//
//        cartItemRepository.delete(item);
//    }
//   @Transactional
//  public void removeItem(Long itemId) {
//    cartItemRepository.deleteByIdCustom(itemId);
//  }
@Transactional
public void removeItem(Long itemId) {
    CartItem item = cartItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));

    Cart cart = item.getCart();

    cartItemRepository.delete(item);

    if (cart.getItems().size() <= 1) {
        cartRepository.delete(cart);
    }
}

    public List<Cart> getUserCart() {
        User user = getCurrentUser();
        return cartRepository.findByUser(user)
                .map(List::of)   // wrap single cart into a list
                .orElse(List.of());
    }
}
