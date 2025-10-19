package accesadades.act1.act1.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import accesadades.act1.act1.demo.model.Customer;

@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer c = new Customer();
            c.setId(rs.getLong("id"));
            c.setNom(rs.getString("nom"));
            c.setCognom(rs.getString("cognom"));
            c.setAge(rs.getInt("age"));
            c.setCicle(rs.getString("cicle"));
            c.setAny(rs.getInt("any_year"));
            return c;
        }
    }

    public List<Customer> findAll() {
        String sql = "SELECT * FROM students";
        return jdbcTemplate.query(sql, new CustomerRowMapper());
    }

    public void saveBatch() {
        String sql = "INSERT INTO students (nom, cognom, age, cicle, any_year) VALUES (?, ?, ?, ?, ?)";
        for (int i = 1; i <= 10; i++) {
            jdbcTemplate.update(sql,
                    "Nom" + i,
                    "Cognom" + i,
                    18 + i,
                    "DAM",
                    2025);
        }
    }
}
