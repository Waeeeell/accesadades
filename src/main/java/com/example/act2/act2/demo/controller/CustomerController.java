package com.example.act2.act2.demo.controller;

import com.example.act2.act2.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{user_id}/image")
    public ResponseEntity<?> uploadImage(@PathVariable("user_id") Long userId,
            @RequestParam("imageFile") MultipartFile file) {
        try {
            String path = userService.uploadUserImage(userId, file);
            return ResponseEntity.ok("Imagen guardada en: " + path);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsv(@RequestParam("csvFile") MultipartFile file) {
        try {
            int count = userService.uploadUsersCsv(file);
            return ResponseEntity.ok("Registros CSV añadidos: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error CSV: " + e.getMessage());
        }
    }

    @PostMapping("/upload-json")
    public ResponseEntity<?> uploadJson(@RequestParam("jsonFile") MultipartFile file) {
        try {
            int count = userService.uploadUsersJson(file);
            return ResponseEntity.ok("Registros JSON añadidos: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error JSON: " + e.getMessage());
        }
    }
}