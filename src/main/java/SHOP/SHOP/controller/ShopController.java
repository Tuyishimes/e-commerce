package SHOP.SHOP.controller;

import SHOP.SHOP.model.Product;
import SHOP.SHOP.model.Category;
import SHOP.SHOP.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop/api")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    // PRODUCTS
    @GetMapping("/products/public")
    public ResponseEntity<List<Map<String,Object>>> getAllProducts() {
        List<Product> products=shopService.getAllProducts();
        List<Map<String,Object>> Response=products.stream().map(product -> {
            Map<String,Object> map=new LinkedHashMap<>();
            map.put("product_id", product.getId());
            map.put("product_name",product.getName());
            map.put("product_price",product.getPrice());
            map.put("product_Description",product.getDescription());
            map.put("product_image",product.getImageUrl());
            map.put("product_category",product.getCategory());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(Response);
    }

    @GetMapping("/products/public/{id}")
    public ResponseEntity<Map<String,Object>> getProduct(@PathVariable Long id) {
        Product product=shopService.getProductById(id);
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("product_id", product.getId());
        map.put("product_name",product.getName());
        map.put("product_price",product.getPrice());
        map.put("product_Description",product.getDescription());
        map.put("product_image",product.getImageUrl());
        map.put("product_category",product.getCategory());

        return  ResponseEntity.status(200).body(map);
       // return shopService.getProductById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products/admin")
    public ResponseEntity<Map<String,Object>> addProduct(@RequestBody Product product) {
        Product savedProduct=shopService.addProduct(product);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Product added successfully");
        response.put("product_id", savedProduct.getId());
        response.put("product_name", savedProduct.getName());
        response.put("price", savedProduct.getPrice());
        response.put("category", savedProduct.getCategory());

        return ResponseEntity.status(201).body(response);
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
