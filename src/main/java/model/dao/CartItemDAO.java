package model.dao;

import model.dto.CartDTO;
import model.dto.CartItemDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO implements GenericDAO<CartItemDTO, Integer>{
    private final DataSource dataSource;

    public CartItemDAO(DataSource ds) {
        if(ds == null) throw new IllegalArgumentException("DataSource cannot be null.");
        this.dataSource = ds;
    }


    @Override
    public void save(CartItemDTO cartItem) throws SQLException {
        validateCartItem(cartItem);

        String sql = "INSERT INTO CartItem (CartID, ProductID, Quantity) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cartItem.getCartID());
            ps.setInt(2, cartItem.getProductID());
            ps.setInt(3, cartItem.getQuantity());

            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    cartItem.setCartID(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(CartItemDTO cartItem) throws SQLException {
        validateCartItem(cartItem);
        if(cartItem.getId() < 1) throw new IllegalArgumentException("CartItem ID must be positive for update operations.");

        String sql = "UPDATE CartItem SET CartID = ?, ProductID = ?, Quantity = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartItem.getCartID());
            ps.setInt(2, cartItem.getProductID());
            ps.setInt(3, cartItem.getQuantity());
            ps.setInt(4, cartItem.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("ID must be a positive integer.");

        String sql = "DELETE FROM  CartItem WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByCartId(int cartId, Connection connection) throws SQLException {
        if (cartId <= 0) {
            throw new IllegalArgumentException("CartID must be a positive integer.");
        }
        String sql = "DELETE FROM CartItem WHERE CartID = ?"; // Usa la connessione passata
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public CartItemDTO findById(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("ID must be a positive integer.");

        String sql = "SELECT * FROM CartItem WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return extractCartItem(rs);
                }
            }
        }

        return null;
    }

    public CartItemDTO findByProductIDAndCartID(Integer cartID, Integer productID) throws SQLException {
        if(cartID == null || cartID < 1) throw new IllegalArgumentException("CartItem ID must be a positive integer.");
        String sql = "SELECT * FROM CartItem WHERE CartID = ? AND ProductID = ?"; // Query corretta per l'ordine
        System.out.println("--- DEBUG DAO: Eseguo findByProductIDAndCartID ---");
        System.out.println("Cerco CartID: " + cartID + " e ProductID: " + productID);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartID);
            ps.setInt(2, productID);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    System.out.println("-> Trovato item: " + rs.getInt("ID"));
                    return extractCartItem(rs);
                }
            }
        }
        System.out.println("-> NESSUN RISULTATO TROVATO. Restituisco null.");
        return null;
    }

    public List<CartItemDTO> findByCartID(Integer cartID) throws SQLException {
        if(cartID == null || cartID < 1) throw new IllegalArgumentException("CartItem ID must be a positive integer.");

        List<CartItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM CartItem WHERE CartID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCartItem(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<CartItemDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID"; // Default
        }

        List<CartItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM CartItem ORDER BY " + order;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractCartItem(rs));
            }
        }
        return list;
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        return List.of("ID", "CartID", "ProductID", "Quantity");
    }


    private void validateCartItem(CartItemDTO cartItem) throws SQLException {
        if(cartItem == null  ||
         cartItem.getCartID() < 1 || cartItem.getProductID() < 1 ||
         cartItem.getQuantity() < 1)
            throw new IllegalArgumentException("Some required CartItem fields are null or invalid.");
    }

    private CartItemDTO extractCartItem(ResultSet rs) throws SQLException {
        CartItemDTO cartItem = new CartItemDTO();
        cartItem.setId(rs.getInt("ID"));
        cartItem.setCartID(rs.getInt("CartID"));
        cartItem.setProductID(rs.getInt("ProductID"));
        cartItem.setQuantity(rs.getInt("Quantity"));

        return cartItem;
    }
}
