package com.example.category.Consumer;

import com.example.category.Model.Category;
import com.example.category.Model.Request.CategoryCheckRequest;
import com.example.category.Model.Request.CategoryRequest;
import com.example.category.Repository.CategoryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryConsumer {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = "${rabbitmq.category.queue.name}")
    public Boolean findCategory(String request) {
        System.out.println("Consumer Called with: " + request);
        System.out.println("Result -> " + categoryRepository.existsByNameIgnoreCase(request));
        return categoryRepository.existsByNameIgnoreCase(request);
    }

    @RabbitListener(queues = "${product_suggestion_queue}")
    public List<String> getCategory(String category){
        List<String> categoryList = new ArrayList<>();Optional<Category> existingCategory = categoryRepository.findByName(category);
        List<Category> subCategories = categoryRepository.findByParentCategory_Id(existingCategory.get().getId());
        for(Category i : subCategories ) {
            categoryList.add(i.getName());
        }
        categoryList.add(category);
        return categoryList;
    }
}
