package com.example.shop.Controller;

import com.example.shop.Model.Shop;
import com.example.shop.Service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping
    public ResponseEntity<Shop> createShop(@RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.createShop(shop));
    }

    @GetMapping
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Shop>> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.getShopById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, @RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.updateShop(id, shop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.ok("Shop deleted successfully.");
    }
}

