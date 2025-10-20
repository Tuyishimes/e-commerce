package SHOP.SHOP.repository;

import SHOP.SHOP.model.CartItem;
import SHOP.SHOP.model.Cart;
import SHOP.SHOP.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.id = :itemId")
    void deleteByIdCustom(@Param("itemId") Long itemId);
}
