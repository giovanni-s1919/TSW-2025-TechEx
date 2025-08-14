package model.dao;

import model.dto.CartDTO;
import model.dto.CartItemDTO;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO extends AbstractDAO<CartDTO, Integer>{

    public CartDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(CartDTO cart) throws SQLException {
        validate(cart);

        String sql = "INSERT INTO Cart (UserID) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cart.getUserID());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cart.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(CartDTO cart) throws SQLException {
        validate(cart);
        if(cart.getId() < 1) throw new IllegalArgumentException("Cart ID must be positive for update operations.");

        String sql = "UPDATE Cart SET UserID = ? WHERE ID = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cart.getUserID());
            ps.setInt(2, cart.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id < 1) throw new IllegalArgumentException("ID must be a positive integer.");

        String sql = "DELETE FROM Cart WHERE ID = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public CartDTO findById(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("ID must be a positive integer.");

        String sql = "SELECT * FROM Cart WHERE ID = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extract(rs);
                }
            }
        }

        return null;
    }
    public CartDTO findByUserID(Integer userID) throws SQLException {
        if(userID == null || userID < 1) throw new IllegalArgumentException("UserID must be a positive integer.");

        String sql = "SELECT * FROM Cart WHERE UserID = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extract(rs);
                }
            }
        }
        return null;
    }


    @Override
    public List<CartDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID";
        }

        List<CartDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM Cart ORDER BY " + order;
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extract(rs));
            }
        }

        return list;
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        return List.of("ID", "UserID");
    }


    @Override
    protected void validate(CartDTO cart)  {
        if (cart == null || cart.getUserID() < 1)
            throw new IllegalArgumentException("Cart or UserID is invalid.");
    }

    @Override
    protected CartDTO extract(ResultSet rs) throws SQLException {
        CartDTO cart = new CartDTO();
        cart.setId(rs.getInt("ID"));
        cart.setUserID(rs.getInt("UserID"));
        return cart;
    }
}
