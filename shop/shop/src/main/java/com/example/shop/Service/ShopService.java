package com.example.shop.Service;

import com.example.shop.Model.Shop;

import java.util.List;
import java.util.Optional;

public interface ShopService {
    Shop createShop(Shop shop);
    List<Shop> getAllShops();
    Optional<Shop> getShopById(Long id);
    Shop updateShop(Long id, Shop updatedShop);
    void deleteShop(Long id);
}
