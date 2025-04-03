package com.example.payment.Controller;

import com.example.payment.Consumer.OrderConsumer;
import com.example.payment.Model.Order;
import com.example.payment.Model.Payment;
import com.example.payment.Model.Response.CommonResponse;
import com.example.payment.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/api/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;
    @Autowired
    OrderConsumer orderConsumer;
    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/process")
    public ResponseEntity<CommonResponse> processPayment(@RequestBody Payment paymentRequest) {
        Order latestOrder = orderConsumer.getLatestOrder();

        System.out.println(latestOrder);
        if (latestOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "No order found for processing payment.", false));
        }

        ResponseEntity<CommonResponse> result = paymentService.processPayment(
                latestOrder.getId(), latestOrder.getTotalPrice(), paymentRequest
        );

        if (result.getBody().getStatusCode() == 200) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            ResponseEntity<CommonResponse> updateStatus = restTemplate.exchange(
                    "http://localhost:8084/api/orders/update-status/" + latestOrder.getId() + "/SUCCESS",
                    HttpMethod.PATCH,
                    entity,
                    CommonResponse.class
            );

            return ResponseEntity.ok(new CommonResponse(200, "Payment processed successfully!", true));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(HttpStatus.BAD_REQUEST.value(), "Payment Failed !!", false));
        }
    }

}
