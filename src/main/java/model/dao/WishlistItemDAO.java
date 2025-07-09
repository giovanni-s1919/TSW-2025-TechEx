package model.dao;

import model.dto.WishlistItemDTO;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistItemDAO extends AbstractDAO<WishlistItemDTO, Integer> {

    public WishlistItemDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(WishlistItemDTO wishlistItem) throws SQLException {
        validate(wishlistItem);

        String sql = "INSERT INTO WishlistItem (WishlistID, ProductID) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, wishlistItem.getWishlistID());
            ps.setInt(2, wishlistItem.getProductID());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    wishlistItem.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(WishlistItemDTO wishlistItem) throws SQLException {
        validate(wishlistItem);
        if (wishlistItem.getId() <= 0) {
            throw new IllegalArgumentException("WishlistItem ID must be positive for update operations.");
        }

        // Updates for WishlistItem are generally rare, as it's typically just a link.
        // If ProductID or WishlistID could be changed for an existing item, this logic is valid.
        String sql = "UPDATE WishlistItem SET WishlistID = ?, ProductID = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, wishlistItem.getWishlistID());
            ps.setInt(2, wishlistItem.getProductID());
            ps.setInt(3, wishlistItem.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("WishlistItem ID must be a positive integer for deletion.");
        }

        String sql = "DELETE FROM WishlistItem WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public WishlistItemDTO findById(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("WishlistItem ID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM WishlistItem WHERE ID = ?";
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

    /**
     * Finds all wishlist items belonging to a specific wishlist.
     * @param wishlistID The ID of the wishlist.
     * @return A list of WishlistItemDTOs.
     * @throws SQLException if a database access error occurs.
     */
    public List<WishlistItemDTO> findByWishlistID(int wishlistID) throws SQLException {
        if (wishlistID <= 0) {
            throw new IllegalArgumentException("WishlistID must be a positive integer.");
        }

        List<WishlistItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM WishlistItem WHERE WishlistID = ? ORDER BY ProductID"; // Order by ProductID for consistency
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, wishlistID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    /**
     * Finds a specific wishlist item by its WishlistID and ProductID.
     * Useful for checking if a product is already in a wishlist or for direct removal.
     * @param wishlistID The ID of the wishlist.
     * @param productID The ID of the product.
     * @return The WishlistItemDTO if found, otherwise null.
     * @throws SQLException if a database access error occurs.
     */
    public WishlistItemDTO findByWishlistAndProduct(int wishlistID, int productID) throws SQLException {
        if (wishlistID <= 0 || productID <= 0) {
            throw new IllegalArgumentException("WishlistID and ProductID must be positive integers.");
        }

        String sql = "SELECT * FROM WishlistItem WHERE WishlistID = ? AND ProductID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, wishlistID);
            ps.setInt(2, productID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extract(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<WishlistItemDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID"; // Default
        }

        List<WishlistItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM WishlistItem ORDER BY " + order;
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
        return List.of("ID", "WishlistID", "ProductID");
    }

    @Override
    protected void validate(WishlistItemDTO wishlistItem) {
        if (wishlistItem == null) {
            throw new IllegalArgumentException("WishlistItem cannot be null.");
        }
        if (wishlistItem.getWishlistID() <= 0) {
            throw new IllegalArgumentException("WishlistID must be a positive integer.");
        }
        if (wishlistItem.getProductID() <= 0) {
            throw new IllegalArgumentException("ProductID must be a positive integer.");
        }
    }

    @Override
    protected WishlistItemDTO extract(ResultSet rs) throws SQLException {
        WishlistItemDTO wishlistItem = new WishlistItemDTO();
        wishlistItem.setId(rs.getInt("ID"));
        wishlistItem.setWishlistID(rs.getInt("WishlistID"));
        wishlistItem.setProductID(rs.getInt("ProductID"));
        return wishlistItem;
    }
}