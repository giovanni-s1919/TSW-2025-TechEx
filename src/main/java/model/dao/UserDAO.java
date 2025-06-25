package model.dao;

import jakarta.servlet.ServletContext;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
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
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM User WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public UserDTO findById(Integer id) throws SQLException {
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

    @Override
    public List<UserDTO> findAll(String order) throws SQLException {
        if(!getAllowedOrderColumns().contains(order)){
            order = "ID";
        }

        List<UserDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM User ORDER BY " + order;
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
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
