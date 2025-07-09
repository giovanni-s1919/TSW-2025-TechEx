package model.dao;

import model.dto.WishlistDTO;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO extends AbstractDAO<WishlistDTO, Integer> {

    public WishlistDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(WishlistDTO wishlist) throws SQLException {
        validate(wishlist);

        String sql = "INSERT INTO Wishlist (UserID) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, wishlist.getUserID());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    wishlist.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(WishlistDTO wishlist) throws SQLException {
        validate(wishlist);
        if (wishlist.getId() <= 0) {
            throw new IllegalArgumentException("Wishlist ID must be positive for update operations.");
        }

        // L'UPDATE qui si basa sull'ID della wishlist, che è la PK.
        // Se si volesse aggiornare anche lo UserID (anche se UNICO, quindi poco comune),
        // si dovrebbe aggiungere al SET. Tuttavia, l'ID è più comune.
        String sql = "UPDATE Wishlist SET UserID = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, wishlist.getUserID());
            ps.setInt(2, wishlist.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Wishlist ID must be a positive integer for deletion.");
        }

        String sql = "DELETE FROM Wishlist WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Metodo specifico per trovare una Wishlist tramite UserID (molto comune per le wishlist)
    public WishlistDTO findByUserID(int userID) throws SQLException {
        if (userID <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM Wishlist WHERE UserID = ?";
        try (Connection connection = dataSource.getConnection();
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
    public WishlistDTO findById(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Wishlist ID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM Wishlist WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
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

    @Override
    public List<WishlistDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID"; // Default
        }

        List<WishlistDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Wishlist ORDER BY " + order;
        try (Connection connection = dataSource.getConnection();
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
    protected void validate(WishlistDTO wishlist) {
        if (wishlist == null) {
            throw new IllegalArgumentException("Wishlist cannot be null.");
        }
        if (wishlist.getUserID() <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer.");
        }
        // Nota: la validazione dell'unicità del UserID è gestita dal DB tramite il vincolo UNIQUE.
        // Qui ci assicuriamo che il valore sia sintatticamente valido.
    }

    @Override
    protected WishlistDTO extract(ResultSet rs) throws SQLException {
        WishlistDTO wishlist = new WishlistDTO();
        wishlist.setId(rs.getInt("ID"));
        wishlist.setUserID(rs.getInt("UserID"));
        return wishlist;
    }
}