package com.example.category.Consumer;

import com.example.category.Model.Request.CategoryCheckRequest;
import com.example.category.Model.Response.CategoryCheckResponse;
import com.example.category.Repository.CategoryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryConsumer {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = "${rabbitmq.category.queue.name}")
    public CategoryCheckResponse findCategory(CategoryCheckRequest request) {
        System.out.println("Consumer Called with: " + request.getCategoryName());

        boolean exists = categoryRepository.existsByNameIgnoreCase(request.getCategoryName());

        CategoryCheckResponse response = new CategoryCheckResponse();
        response.setExists(exists);
        System.out.println(response);
        return response;
    }
}
