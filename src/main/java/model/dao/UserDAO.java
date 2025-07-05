package model.dao;

import jakarta.servlet.ServletContext;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.dto.UserDTO;

public class UserDAO implements GenericDAO<UserDTO, Integer> {
    private final DataSource dataSource;

    public UserDAO(DataSource ds) {
        if(ds == null) throw new IllegalArgumentException("DataSource cannot be null.");
        this.dataSource = ds;
    }


    @Override
    public void save(UserDTO user) throws SQLException {
        validateUser(user);

        String sql = "INSERT INTO User (Username, Email, PasswordHash, Role) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());

            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }

        }
    }

    @Override
    public void update(UserDTO user) throws SQLException {
        validateUser(user);

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
    public boolean delete(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("UserID must be a positive integer.");

        String sql = "DELETE FROM User WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public UserDTO findById(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("UserID must be a positive integer.");

        String sql = "SELECT * FROM User WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return extractUser(rs);
                }
            }
        }
        return null;
    }

    public UserDTO findByEmail(String email) throws SQLException {
        if(email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty.");

        String sql = "SELECT * FROM User WHERE Email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return extractUser(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<UserDTO> findAll(String order) throws SQLException {
        if(!getAllowedOrderColumns().contains(order)){
            order = "ID";
        }

        List<UserDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM User ORDER BY " + order;
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();){
            while(rs.next()){
                list.add(extractUser(rs));
            }
        }

        return list;
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        return List.of("ID", "Username", "Email", "PasswordHash", "Role");
    }


    private void validateUser(UserDTO user) {
        if (user == null ||
                user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty() ||
                user.getRole() == null) {
            throw new IllegalArgumentException("I dati dell'utente non possono essere nulli o vuoti.");
        }
    }

    private UserDTO extractUser(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setId(rs.getInt("ID"));
        user.setUsername(rs.getString("Username"));
        user.setEmail(rs.getString("Email"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setRole(UserDTO.Role.valueOf(rs.getString("Role")));

        return user;
    }
}
