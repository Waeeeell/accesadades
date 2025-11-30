package com.example.act2.act2.demo.services;

import com.example.act2.act2.demo.model.Customer;
import com.example.act2.act2.demo.repository.CustomerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    public UserService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.objectMapper = new ObjectMapper();
    }

    public String uploadUserImage(Long userId, MultipartFile file) throws IOException {

        Customer user = customerRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Path uploadPath = Paths.get("src/main/resources/public/images");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = userId + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String dbPath = filePath.toString();
        customerRepository.updateImagePath(userId, dbPath);

        return dbPath;
    }

    public int uploadUsersCsv(MultipartFile file) throws IOException {
        List<Customer> usersToAdd = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");
                if (data.length >= 4) {
                    Customer c = new Customer();
                    c.setName(data[0].trim());
                    c.setDescription(data[1].trim());
                    c.setEmail(data[2].trim());
                    c.setPassword(data[3].trim());
                    usersToAdd.add(c);
                }
            }
        }

        int count = 0;
        for (Customer c : usersToAdd) {
            count += customerRepository.save(c);
        }

        saveFileToFolder(file, "csv_processed");

        return count;
    }

    public int uploadUsersJson(MultipartFile file) throws IOException {
        JsonNode root = objectMapper.readTree(file.getInputStream());
        JsonNode dataNode = root.path("data");

        String control = dataNode.path("control").asText();
        int countDeclared = dataNode.path("count").asInt();
        JsonNode usersNode = dataNode.path("users");

        if (!"OK".equals(control) || usersNode.size() != countDeclared) {
            throw new RuntimeException("JSON inválido: Control no es OK o el count no coincide.");
        }

        int savedCount = 0;
        if (usersNode.isArray()) {
            for (JsonNode node : usersNode) {
                Customer c = new Customer();
                c.setName(node.path("name").asText());
                c.setDescription(node.path("description").asText());
                c.setEmail(node.path("email").asText());
                c.setPassword(node.path("password").asText());
                savedCount += customerRepository.save(c);
            }
        }

        saveFileToFolder(file, "json_processed");

        return savedCount;
    }

    private void saveFileToFolder(MultipartFile file, String folderName) throws IOException {
        Path path = Paths.get(folderName);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING);
    }
}