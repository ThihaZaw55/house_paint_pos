package com.thz.house_paint.api.management.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    // Target Folder Path: project_root/uploads/products
    private final Path uploadPath = Paths.get("uploads/products");
    private static final List<String> ALLOWED_CONTENT_TYPE = Arrays.asList("image/jpg", "image/jpeg", "image/png", "image/webp");
    private static final long MAX_FILE_SIZE = 6*1024*1012;
    
    public FileStorageService() {
        try {
            // Folder မရှိသေးပါက အလိုအလျောက် ဆောက်ပေးမည်
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage folder!", e);
        }
    }

    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        // 2. File Size Limit စစ်ဆေးခြင်း
        if(file.getSize() > MAX_FILE_SIZE) {
        	throw new IllegalArgumentException("File size exceeds maximum limit of 6MB");
        }
        
     // 3. Content-Type (MIME Type) စစ်ဆေးခြင်း
        String contentType = file.getContentType();
        if(contentType == null || !ALLOWED_CONTENT_TYPE.contains(contentType.toLowerCase())) {
        	throw new IllegalArgumentException("Invalid file type. Only JPG, PNG, and WEBP are allowed.");
        }
        try {
            // 1. File Extension ကို ယူခြင်း (.jpg, .png, etc.)
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            if(!List.of(".jpg", ".jpeg", ".png", ".webp").contains(extension)) {
            	throw new IllegalArgumentException("Invalid file extension");
            }
            // 2. File Name ထပ်မသွားစေရန် UUID ဖြင့် နာမည်အသစ်ပေးခြင်း
            String newFileName = UUID.randomUUID().toString() + extension;

            // 3. Folder ထဲသို့ File ကူးယူ သိမ်းဆည်းခြင်း
            Path destination = this.uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            // 4. Database ထဲသိမ်းရန် Relative Web Path ပြန်ပေးခြင်း
            return "/uploads/products/" + newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void deleteFile(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try {
            // 1. Path ထဲမှ File Name သီးသန့်ထုတ်ယူခြင်း (e.g. "xyz.jpg")
            String fileName = Paths.get(imageUrl).getFileName().toString();

            // 2. Absolute Path ရှာဖွေခြင်း
            Path targetFolder = this.uploadPath.toAbsolutePath().normalize();
            Path filePath = targetFolder.resolve(fileName).normalize();

            // 3. Path Traversal Attack မှ ကာကွယ်ရန် Safe Path စစ်ဆေးခြင်း
            if (!filePath.startsWith(targetFolder)) {
                throw new SecurityException("Cannot delete file outside target directory.");
            }

            // 4. File ရှိပါက ဖျက်ပစ်ခြင်း
            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            // Log သို့မဟုတ် Exception ထုတ်ပေးနိုင်သည်
            System.err.println("Failed to delete file: " + imageUrl + " -> " + e.getMessage());
        }
    }
}