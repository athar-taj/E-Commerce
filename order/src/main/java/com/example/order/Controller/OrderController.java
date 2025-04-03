package com.example.order.Controller;

import com.example.order.Model.Order;
import com.example.order.Model.Response.CommonResponse;
import com.example.order.Producer.OrderProducer;
import com.example.order.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProducer orderProducer;
    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/place")
    public ResponseEntity<CommonResponse> placeOrder(@RequestBody Order order) {
        Boolean isAvailable = restTemplate.getForObject(
                "http://localhost:8082/api/products/check-stock/" + order.getProductId() + "/" + order.getQuantity(),
                Boolean.class
        );

        if (Boolean.TRUE.equals(isAvailable)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<CommonResponse> updateStock = restTemplate.exchange(
                    "http://localhost:8082/api/products/update-stock/" + order.getProductId() + "/" + order.getQuantity(),
                    HttpMethod.PATCH,
                    entity,
                    CommonResponse.class
            );
            return orderService.placeOrder(order);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(HttpStatus.BAD_REQUEST.value(), "Order failed: Product out of stock!", false));
        }
    }

    @PatchMapping("/update-status/{orderId}/{status}")
    public ResponseEntity<CommonResponse> updateOrderStatus(@PathVariable("orderId") Long orderId,@PathVariable("status") String status){
        return orderService.updateStatus(orderId,status);
    }
}
