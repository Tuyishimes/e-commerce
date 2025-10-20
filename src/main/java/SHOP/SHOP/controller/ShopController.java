package SHOP.SHOP.controller;

import SHOP.SHOP.model.Product;
import SHOP.SHOP.model.Category;
import SHOP.SHOP.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/api")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    // PRODUCTS
    @GetMapping("/products/public")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(shopService.getAllProducts());
    }

    @GetMapping("/products/public/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return shopService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products/admin")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(shopService.addProduct(product));
    }

    @PutMapping("/products/admin/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(shopService.updateProduct(id, product));
    }

    @DeleteMapping("/products/admin/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        shopService.deleteProduct(id);
        return ResponseEntity.ok("DELETE"+id);
    }

    // CATEGORIES
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(shopService.getAllCategories());
    }
}
