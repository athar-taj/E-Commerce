package com.example.order.Controller;

import com.example.order.Model.Order;
import com.example.order.Model.Request.OrderRequest;
import com.example.order.Model.Request.StockRequest;
import com.example.order.Model.Response.CommonResponse;
import com.example.order.Producer.OrderProducer;
import com.example.order.Service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    RabbitTemplate rabbitTemplate;

    @PostMapping("/place")
    public ResponseEntity<CommonResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        StockRequest stockRequest = new StockRequest();
        stockRequest.setProductId(orderRequest.getProductId());
        stockRequest.setQuantity(orderRequest.getQuantity());

        Boolean isStockAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
                "stock_exchange", "stock_routing_key", stockRequest);

        if (Boolean.FALSE.equals(isStockAvailable)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(HttpStatus.BAD_REQUEST.value(),
                            "Product out of stock!", false));
        }
        ResponseEntity<CommonResponse> response = orderService.placeOrder(orderRequest);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            rabbitTemplate.convertAndSend("stock_exchange", "stock_reduce_key", stockRequest);
        }

        return response;
    }

    @PatchMapping("/update-status/{orderId}/{status}")
    public ResponseEntity<CommonResponse> updateOrderStatus(@PathVariable("orderId") Long orderId,@PathVariable("status") String status){
        return orderService.updateStatus(orderId,status);
    }
}
