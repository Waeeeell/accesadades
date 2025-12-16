package com.example.act2.act2.demo.controller;

import com.example.act2.act2.demo.model.Customer;
import com.example.act2.act2.demo.repository.CustomerRepository;
import com.example.act2.act2.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private final UserService userService;
    private final CustomerRepository customerRepository;

    public CustomerController(UserService userService, CustomerRepository customerRepository) {
        this.userService = userService;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllUsers() {
        List<Customer> users = customerRepository.findAll();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Customer> getUserById(@PathVariable long userId) {
        List<Customer> users = customerRepository.findOne(userId);

        if (users == null || users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users.get(0));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody Customer customer) {
        int result = customerRepository.insertUser(customer);

        if (result > 0) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario insertado correctamente: " + customer.getName());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar el usuario");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Customer> updateUser(@PathVariable long userId, @RequestBody Customer customer) {
        int updatedRows = customerRepository.updateUser(userId, customer);

        if (updatedRows == 0) {
            return ResponseEntity.notFound().build();
        }

        List<Customer> updatedUsers = customerRepository.findOne(userId);
        if (updatedUsers == null || updatedUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(updatedUsers.get(0));
    }

    @PatchMapping("/{userId}/name")
    public ResponseEntity<String> updateUserName(@PathVariable long userId, @RequestParam String name) {
        if (name.length() > 100) {
            return ResponseEntity.badRequest().body("El nombre no puede tener más de 100 caracteres");
        }

        int updatedRows = customerRepository.updateUserPatch(userId, name);

        if (updatedRows == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con la id " + userId + " no se ha encontrado");
        }

        List<Customer> users = customerRepository.findOne(userId);
        String currentName = (users != null && !users.isEmpty()) ? users.get(0).getName() : name;

        return ResponseEntity.ok("Actualizado correctamente nombre: " + currentName);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) {
        List<Customer> users = customerRepository.findOne(userId);

        if (users == null || users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con la id " + userId + " no fue encontrado");
        }

        Customer userToDelete = users.get(0);
        int deletedRows = customerRepository.deleteUser(userId);

        if (deletedRows >= 1) {
            return ResponseEntity.ok("Eliminado correctamente: " + userToDelete.getName());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el usuario");
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