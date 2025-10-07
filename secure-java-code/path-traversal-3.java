package com.devteria.file.controller;

import com.devteria.file.dto.ApiResponse;
import com.devteria.file.dto.response.FileResponse;
import com.devteria.file.service.FileService;
// ... các imports khác ...
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths; // Import mới

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;

    // ... (Hàm uploadMedia giữ nguyên) ...

    @GetMapping("/media/download/{fileName}")
    ResponseEntity<Resource> downloadMedia(@PathVariable String fileName) throws IOException {
        
        // --- MÃ FIX: Xử lý Path Traversal ---
        String safeFileName;
        try {
            // 1. Chuẩn hóa tên file bằng Paths.get().normalize() 
            //    Loại bỏ các thành phần như ../, ./
            safeFileName = Paths.get(fileName).normalize().toString();
            
            // 2. Kiểm tra nếu tên file đã bị thay đổi (ví dụ: mất ../)
            //    Kiểm tra nếu tên file sau khi normalize có dấu / (path separator) không
            if (safeFileName.contains("..") || safeFileName.startsWith("/") || safeFileName.startsWith("\\")) {
                 // Nếu tên file vẫn chứa '..' (sau khi normalize) hoặc bắt đầu bằng dấu phân cách, 
                 // nó là một nỗ lực Path Traversal.
                 throw new InvalidPathException(fileName, "Invalid filename or path.");
            }
            
        } catch (InvalidPathException e) {
            // Trả về lỗi 400 Bad Request nếu tên file không hợp lệ
            return ResponseEntity.badRequest().build();
        }
        
        // Dữ liệu đã được làm sạch được truyền vào service
        var fileData = fileService.download(safeFileName);

        return ResponseEntity.<Resource>ok()
                .header(HttpHeaders.CONTENT_TYPE, fileData.contentType())
                .body(fileData.resource());
    }
}