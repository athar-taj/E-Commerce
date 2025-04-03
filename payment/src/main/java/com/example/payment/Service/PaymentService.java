package com.example.payment.Service;

import com.example.payment.Model.Payment;
import com.example.payment.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<CommonResponse> processPayment(long orderId, double amount, Payment payment);
}
