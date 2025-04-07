package com.example.order.Model;

import com.example.order.Model.Request.OrderRequest;

public class OrderWrapper {
    private OrderRequest orderRequest;
    private Product product;

    public OrderWrapper(OrderRequest orderRequest, Product product) {
        this.orderRequest = orderRequest;
        this.product = product;
    }

    public OrderWrapper() {
    }

    public OrderRequest getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(OrderRequest orderRequest) {
        this.orderRequest = orderRequest;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
