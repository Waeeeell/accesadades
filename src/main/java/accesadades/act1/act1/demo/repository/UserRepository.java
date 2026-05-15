package accesadades.act1.act1.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import accesadades.act1.act1.demo.model.User;

/**
 * Repositori per a gestionar les operacions de base de dades de la taula users
 */
@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * RowMapper per a convertir ResultSet a objecte User
     */
    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setDescription(rs.getString("description"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));

            // Gestionar timestamps nuls
            Timestamp ultimAcces = rs.getTimestamp("ultimAcces");
            if (ultimAcces != null) {
                user.setUltimAcces(ultimAcces.toLocalDateTime());
            }

            Timestamp dataCreated = rs.getTimestamp("dataCreated");
            if (dataCreated != null) {
                user.setDataCreated(dataCreated.toLocalDateTime());
            }

            Timestamp dataUpdated = rs.getTimestamp("dataUpdated");
            if (dataUpdated != null) {
                user.setDataUpdated(dataUpdated.toLocalDateTime());
            }

            return user;
        }
    }

    /**
     * Obtenir tots els usuaris de la base de dades
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    /**
     * Obtenir un usuari per ID
     */
    public User findById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * Crear un nou usuari
     */
    public void save(User user) {
        String sql = "INSERT INTO users (name, description, email, password, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, NOW(), NOW())";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getDescription(),
                user.getEmail(),
                user.getPassword());
    }

    /**
     * Actualitzar completament un usuari existente
     */
    public int update(long id, User user) {
        String sql = "UPDATE users SET name = ?, description = ?, email = ?, password = ?, dataUpdated = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getDescription(),
                user.getEmail(),
                user.getPassword(),
                id);
    }

    /**
     * Actualitzar només el nom d'un usuari
     */
    public int updateName(long id, String name) {
        String sql = "UPDATE users SET name = ?, dataUpdated = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, name, id);
    }

    /**
     * Eliminar un usuari per ID
     */
    public int delete(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
