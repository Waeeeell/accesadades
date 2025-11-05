package com.example.act2.act2.demo.controller;

import com.example.act2.act2.demo.model.Customer;
import com.example.act2.act2.demo.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Customer c) {
        repository.save(c);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuari creat correctament");
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(repository.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Customer c) {
        repository.update(c, id);
        return ResponseEntity.ok("Usuari actualitzat correctament");
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<String> updateName(@PathVariable Long id, @RequestParam String name) {
        repository.updateName(id, name);
        return ResponseEntity.ok("Nom actualitzat correctament");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        repository.delete(id);
        return ResponseEntity.ok("Usuari eliminat correctament");
    }
}
