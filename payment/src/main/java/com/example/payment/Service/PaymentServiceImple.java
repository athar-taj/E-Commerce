package com.example.payment.Service;

import com.example.payment.Consumer.OrderConsumer;
import com.example.payment.Model.Payment;
import com.example.payment.Model.Response.CommonResponse;
import com.example.payment.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImple implements PaymentService{

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderConsumer orderConsumer;

    public ResponseEntity<CommonResponse> processPayment(long orderId, double amount, Payment paymentRequest) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setAccountNo(paymentRequest.getAccountNo());
        payment.setPayerName(paymentRequest.getPayerName());
        payment.setPayerEmail(paymentRequest.getPayerEmail());
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        orderConsumer.setLatestOrder(null);
        paymentRepository.save(payment);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Payment Successful !!", payment));
    }
}
