package SHOP.SHOP.repository;

import SHOP.SHOP.model.CartItem;
import SHOP.SHOP.model.Cart;
import SHOP.SHOP.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
