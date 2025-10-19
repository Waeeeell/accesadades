package accesadades.act1.act1.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import accesadades.act1.act1.demo.model.Customer;
import accesadades.act1.act1.demo.repository.CustomerRepository;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/students/batch")
    public String addStudents() {
        customerRepository.saveBatch();
        return "10 alumnes inserits correctament!";
    }

    @GetMapping("/students")
    public List<Customer> getStudents() {
        return customerRepository.findAll();
    }
}
