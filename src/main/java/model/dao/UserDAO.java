package model.dao;

import jakarta.servlet.ServletContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import model.dto.UserDTO;

public class UserDAO implements GenericDAO<UserDTO, Integer> {
    private final DataSource dataSource;

    public UserDAO(ServletContext sc) {
        this.dataSource = (DataSource) sc.getAttribute("datasource");
    }


    @Override
    public void save(UserDTO user) throws SQLException {
        if(user == null || user.getUsername() == null || user.getUsername().trim().isEmpty() ||
        user.getEmail() == null || user.getEmail().trim().isEmpty() ||
        user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty() ||
        user.getRole() == null){
            throw new IllegalArgumentException("Data cannot be null or empty");
        }

        String sql = "INSERT INTO User (Username, Email, PasswordHash, Role) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());
        }
    }

    @Override
    public void update(UserDTO user) throws SQLException {
        if(user == null || user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty() ||
                user.getRole() == null){
            throw new IllegalArgumentException("Data cannot be null or empty");
        }

        String sql = "UPDATE User SET Username = ?, Email = ?, PasswordHash = ?, Role = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());
            ps.setInt(5, user.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(UserDTO entity) throws SQLException {

    }

    @Override
    public UserDTO findById(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List findAll() throws SQLException {
        return null;
    }
}
