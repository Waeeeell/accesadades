package accesadades.act1.act1.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")

public class CustomerController {

    @PostMapping("/students/batch")
    public String addStudents() {
        return "Endpoint post";
    }

    @GetMapping("/students")
    public String getStudents() {
        return "Endpoint get";
    }

}
