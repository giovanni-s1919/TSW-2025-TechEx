package model.dao;

import model.dto.CartDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class CartDAO implements GenericDAO<CartDTO, Integer>{
    private final DataSource dataSource;

    public CartDAO(DataSource ds) {
        if(ds == null) throw new IllegalArgumentException("DataSource cannot be null.");
        this.dataSource = ds;
    }

    @Override
    public void save(CartDTO cart) throws SQLException {
        validateCart(cart);

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
    public void update(CartDTO entity) throws SQLException {
        //TODO
    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        //TODO
        return false;
    }

    @Override
    public CartDTO findById(Integer integer) throws SQLException {
        //TODO
        return null;
    }

    @Override
    public List<CartDTO> findAll(String order) throws SQLException {
        //TODO
        return List.of();
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        //TODO
        return List.of("ID", "UserID");
    }


    private void validateCart(CartDTO cart)  {
        if (cart == null || cart.getUserID() < 1)
            throw new IllegalArgumentException("Cart or UserID is invalid.");
    }

    private CartDTO extractCart(ResultSet rs) throws SQLException {
        CartDTO cart = new CartDTO();
        cart.setId(rs.getInt("ID"));
        cart.setUserID(rs.getInt("UserID"));
        return cart;
    }
}
