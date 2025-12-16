package com.example.act2.act2.demo.repository;

import com.example.act2.act2.demo.model.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Customer c) {
        String sql = "INSERT INTO users (name, description, email, password, data_created, data_updated) VALUES (?, ?, ?, ?, NOW(), NOW())";
        return jdbcTemplate.update(sql, c.getName(), c.getDescription(), c.getEmail(), c.getPassword());
    }

    public List<Customer> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Customer.class));
    }

    public Customer findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Customer.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Customer> findOne(Long id) {
        Customer customer = findById(id);
        if (customer != null) {
            return Collections.singletonList(customer);
        }
        return Collections.emptyList();
    }

    public int insertUser(Customer customer) {
        return save(customer);
    }

    public int updateImagePath(Long id, String path) {
        String sql = "UPDATE users SET image_path = ? WHERE id = ?";
        return jdbcTemplate.update(sql, path, id);
    }

    public int update(Customer c, Long id) {
        String sql = "UPDATE users SET name=?, description=?, email=?, password=?, data_updated=NOW() WHERE id=?";
        return jdbcTemplate.update(sql, c.getName(), c.getDescription(), c.getEmail(), c.getPassword(), id);
    }

    public int updateUser(Long id, Customer customer) {
        return update(customer, id);
    }

    public int updateUserPatch(long id, String name) {
        String sql = "UPDATE users SET name=?, data_updated=NOW() WHERE id=?";
        return jdbcTemplate.update(sql, name, id);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM users WHERE id=?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteUser(Long id) {
        return delete(id);
    }
}