package com.example.product.Service.OtherImpl;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileStorage {
    private static String dir = "Z:\\Spring Boot\\Ecommerce\\product\\product\\src\\main\\resources\\Files\\";
    public static String saveFile(MultipartFile file,String type) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot save empty file.");
        }

        String uniqueFileName = generateUniqueFileName() + "_" + file.getOriginalFilename();

        File uploadDir = new File(dir + type + "\\");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Path filePath = Path.of(dir + type + "\\" + uniqueFileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    private static String generateUniqueFileName()
    {
        UUID uuid = UUID.randomUUID();
        return  uuid.toString();
    }
}