package com.example.order.Consumer;

import com.example.order.Model.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

//    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"})
//    public void OrderConsumer(Order order){
//        System.out.println("Consume JSON -> " + order.toString());
//    }


    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @RabbitListener(queues = {"${rabbitmq.product.queue.name}"})
    public void ProductConsumer(Product product){
        setProduct(product);
        System.out.println("Consume JSON -> " + product.toString());
    }


    public static int discountedPrice;

    @RabbitListener(queues = {"get_product_id"})
    public void ProductDiscountPriceConsumer(Product product){
        discountedPrice = product.getPrice() - ((int) (product.getPrice() * product.getDiscount()) / 100);
        System.out.println(discountedPrice);
    }

}
