package com.example.product.ServiceImple;

import com.example.product.Model.Product;
import com.example.product.Model.Request.VariantAttributeRequest;
import com.example.product.Model.Request.VariantRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Model.Response.ProductResponse;
import com.example.product.Model.Response.VariantAttributeResponse;
import com.example.product.Model.Response.VariantResponse;
import com.example.product.Model.Variant;
import com.example.product.Model.VariantAttribute;
import com.example.product.Repository.ProductRepository;
import com.example.product.Repository.VariantAttributeRepository;
import com.example.product.Repository.VariantRepository;
import com.example.product.Service.OtherImpl.FileStorage;
import com.example.product.Service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VariantAttributeRepository variantAttributeRepository;

    @Override
    public ResponseEntity<CommonResponse> createVariant(VariantRequest request, MultipartFile image) throws IOException {
        Optional<Product> product = productRepository.findById(request.getProductId());
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product Not Found", "Product ID: " + request.getProductId()));
        }

        Variant variant = new Variant();
        variant.setProduct(product.get());
        variant.setVariantName(request.getVariantName());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock());
        variant.setImageUrl(FileStorage.saveFile(image, "Variant"));
        variant.setCreatedAt(LocalDateTime.now());

        Variant savedVariant = variantRepository.save(variant);

        if (request.getAttributes() != null) {
            List<VariantAttribute> attributes = new ArrayList<>();
            for (VariantAttributeRequest attribute : request.getAttributes()) {
                VariantAttribute attr = new VariantAttribute();
                attr.setVariant(savedVariant);
                attr.setVariantType(attribute.getVariantType());
                attr.setVariantValue(attribute.getVariantValue());
                attributes.add(attr);
            }

            variantAttributeRepository.saveAll(attributes);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "Variant created successfully", "Variant ID: " + savedVariant.getId()));
    }

    @Transactional
    @Override
    public ResponseEntity<CommonResponse> updateVariant(Long id, VariantRequest request, MultipartFile image) throws IOException {
        Optional<Variant> existsVariant = variantRepository.findById(id);
        if (existsVariant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Variant Not Found !!", "Variant Not found With ID: " + id));
        }

        Variant variant = existsVariant.get();
        variant.setVariantName(request.getVariantName());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock());

        if (image != null && !image.isEmpty()) {
            variant.setImageUrl(FileStorage.saveFile(image, "Variant"));
        }

        variantRepository.save(variant);

        variantAttributeRepository.deleteByVariantId(variant.getId());

        List<VariantAttribute> attributes = new ArrayList<>();
        for (VariantAttributeRequest attrReq : request.getAttributes()) {
            VariantAttribute attr = new VariantAttribute();
            attr.setVariant(variant);
            attr.setVariantType(attrReq.getVariantType());
            attr.setVariantValue(attrReq.getVariantValue());
            attributes.add(attr);
        }
        variantAttributeRepository.saveAll(attributes);

        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), "Variant updated successfully", "Updated variant ID: " + variant.getId()));
    }

    @Override
    public ResponseEntity<CommonResponse> deleteVariant(Long id) {
        variantRepository.deleteById(id);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), "Variant deleted successfully", "Deleted variant ID: " + id));
    }

    @Override
    public ResponseEntity<CommonResponse> getProductWithVariants(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product Not Found", "Product Not found with ID: " + id));
        }

        Product product = optionalProduct.get();

        List<Variant> variants = variantRepository.findByProductId(product.getId());

        List<VariantResponse> variantResponses = new ArrayList<>();

        for (Variant variant : variants) {
            List<VariantAttribute> attributes = variantAttributeRepository.findAllByVariantId(variant.getId());

            List<VariantAttributeResponse> attributeResponseList = new ArrayList<>();
            for (VariantAttribute attribute : attributes) {
                VariantAttributeResponse attributeRes = new VariantAttributeResponse();
                attributeRes.setVariantType(attribute.getVariantType());
                attributeRes.setVariantValue(attribute.getVariantValue());
                attributeResponseList.add(attributeRes);
            }

            VariantResponse variantResp = new VariantResponse();
            variantResp.setVariantName(variant.getVariantName());
            variantResp.setPrice(variant.getPrice());
            variantResp.setStock(variant.getStock());
            variantResp.setImageUrl(variant.getImageUrl());
            variantResp.setAttributes(attributeResponseList);

            variantResponses.add(variantResp);
        }

        ProductResponse productResp = new ProductResponse();
        productResp.setName(product.getName());
        productResp.setPrice(product.getPrice());
        productResp.setCategory(product.getCategory());
        productResp.setBrand(product.getBrand());
        productResp.setImageUrl(product.getImageUrl());
        productResp.setVariants(variantResponses);

        return ResponseEntity.ok(new CommonResponse(200, "Product with Variants fetched", productResp));
    }

    @Override
    public ResponseEntity<CommonResponse> getProductUsingVariants(Long productId, String color, String size, String storage, String quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product not found !!", null));
        }

        Product product = productOpt.get();
        List<Variant> variants = variantRepository.findByProductId(productId);
        List<VariantResponse> matchedVariants = new ArrayList<>();

        for (Variant variant : variants) {
            List<VariantAttribute> attributes = variantAttributeRepository.findAllByVariantId(variant.getId());

            boolean matches = true;
            if (color != null && isAttributeAvailable(attributes,color)) { matches = false; }
            if (size != null && isAttributeAvailable(attributes,size)) { matches = false; }
            if (storage != null && isAttributeAvailable(attributes,storage)) { matches = false; }
            if (quantity != null && isAttributeAvailable(attributes,quantity)) { matches = false; }

            if (matches) {
                List<VariantAttributeResponse> attrResponses = new ArrayList<>();
                for (VariantAttribute attr : attributes) {
                    VariantAttributeResponse attrResp = new VariantAttributeResponse();
                    attrResp.setVariantType(attr.getVariantType());
                    attrResp.setVariantValue(attr.getVariantValue());
                    attrResponses.add(attrResp);
                }

                VariantResponse vr = new VariantResponse();
                vr.setVariantName(variant.getVariantName());
                vr.setPrice(variant.getPrice());
                vr.setStock(variant.getStock());
                vr.setImageUrl(variant.getImageUrl());
                vr.setAttributes(attrResponses);

                matchedVariants.add(vr);
            }
        }

        ProductResponse response = new ProductResponse();
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setBrand(product.getBrand());
        response.setImageUrl(product.getImageUrl());
        response.setVariants(matchedVariants);

        return ResponseEntity.ok(new CommonResponse(200, "Product with Variants fetched", response));
    }

    private boolean isAttributeAvailable(List<VariantAttribute> attributes, String value) {
        for (VariantAttribute attr : attributes) {
            if (attr.getVariantValue().equalsIgnoreCase(value)) {
                return false;
            }
        }
        return true;
    }
}