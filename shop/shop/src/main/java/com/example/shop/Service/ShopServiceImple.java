package com.example.shop.Service;

import com.example.shop.Model.Shop;
import com.example.shop.Repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImple implements ShopService  {

    @Autowired
    private ShopRepository shopRepository;
    public Shop createShop(Shop shop) {
        return shopRepository.save(shop);
    }

    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    public Optional<Shop> getShopById(Long id) {
        return shopRepository.findById(id);
    }

    public Shop updateShop(Long id, Shop shop) {
        Optional<Shop> existingShop = shopRepository.findById(id);
        Shop shop1 = existingShop.get();

        shop1.setAddress(shop.getAddress());
        shop1.setName(shop.getName());
        shop1.setOwner(shop.getOwner());

        shopRepository.save(shop1);
        return shop1;
    }

    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }
}
