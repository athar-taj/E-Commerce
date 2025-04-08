package com.example.product.Model.Response;

public class CategoryCheckResponse {
    private boolean exists;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    @Override
    public String toString() {
        return "CategoryCheckResponse{" +
                "exists=" + exists +
                '}';
    }
}
