package accesadades.act1.act1.demo.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import accesadades.act1.act1.demo.model.User;
import accesadades.act1.act1.demo.repository.UserRepository;

/**
 * Controlador REST per a gestionar les operacions d'usuaris
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * POST: Crear un nou usuari
     * Endpoint: /api/users
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody User user) {
        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuari creat correctament!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET: Obtenir tots els usuaris
     * Endpoint: /api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /**
     * GET: Obtenir un usuari per ID
     * Endpoint: /api/users/{user_id}
     */
    @GetMapping("/{user_id}")
    public ResponseEntity<User> getById(@PathVariable Long user_id) {
        User user = userRepository.findById(user_id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /**
     * PUT: Actualitzar completament un usuari
     * Endpoint: /api/users/{user_id}
     */
    @PutMapping("/{user_id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long user_id, @RequestBody User user) {
        int result = userRepository.update(user_id, user);
        Map<String, String> response = new HashMap<>();

        if (result > 0) {
            response.put("message", "Usuari actualitzat correctament!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("message", "Usuari no trobat");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * PATCH: Actualitzar només el nom d'un usuari
     * Endpoint: /api/users/{user_id}/name
     */
    @PatchMapping("/{user_id}/name")
    public ResponseEntity<User> updateName(@PathVariable Long user_id, @RequestParam String name) {
        User user = userRepository.findById(user_id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        userRepository.updateName(user_id, name);
        user.setName(name);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /**
     * DELETE: Eliminar un usuari
     * Endpoint: /api/users/{user_id}
     */
    @DeleteMapping("/{user_id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long user_id) {
        int result = userRepository.delete(user_id);
        Map<String, String> response = new HashMap<>();

        if (result > 0) {
            response.put("message", "Usuari eliminat correctament!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("message", "Usuari no trobat");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
