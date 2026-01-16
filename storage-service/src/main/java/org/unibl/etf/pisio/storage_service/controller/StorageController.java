package org.unibl.etf.pisio.storage_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.pisio.storage_service.service.StorageService;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("WELCOME");
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        System.out.println("CONTENT TYPE - " + file.getContentType());
        System.out.println("ORIGINAL FILE NAME - " + file.getOriginalFilename());
        String url = storageService.upload(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/presigned/{filename}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String filename) {
        if ("N/A".equals(filename))
            return ResponseEntity.ok("N/A");
        String url = storageService.getPresignedUrl(filename, 3600);
        System.out.println("PRESIGNED URL: " + url);
        return ResponseEntity.ok(url);
    }
}
