package model.dao;

import model.dto.ReviewDTO;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO extends AbstractDAO<ReviewDTO, Integer> {

    public ReviewDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(ReviewDTO review) throws SQLException {
        validate(review);

        String sql = "INSERT INTO Review (UserID, ProductID, Title, Description, Rating) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, review.getUserID());
            ps.setInt(2, review.getProductID());
            ps.setString(3, review.getTitle());
            ps.setString(4, review.getDescription());
            ps.setInt(5, review.getRating()); // TINYINT in DB, int in Java

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(ReviewDTO review) throws SQLException {
        validate(review);
        if (review.getId() <= 0) {
            throw new IllegalArgumentException("Review ID must be positive for update operations.");
        }

        String sql = "UPDATE Review SET UserID = ?, ProductID = ?, Title = ?, Description = ?, Rating = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, review.getUserID());
            ps.setInt(2, review.getProductID());
            ps.setString(3, review.getTitle());
            ps.setString(4, review.getDescription());
            ps.setInt(5, review.getRating());
            ps.setInt(6, review.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Review ID must be a positive integer for deletion.");
        }

        String sql = "DELETE FROM Review WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public ReviewDTO findById(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Review ID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM Review WHERE ID = ?";
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

    // Metodo per trovare tutte le recensioni di un utente specifico
    public List<ReviewDTO> findByUserID(int userID) throws SQLException {
        if (userID <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer.");
        }

        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Review WHERE UserID = ? ORDER BY ID";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    // Metodo per trovare tutte le recensioni di un prodotto specifico
    public List<ReviewDTO> findByProductID(int productID) throws SQLException {
        if (productID <= 0) {
            throw new IllegalArgumentException("ProductID must be a positive integer.");
        }

        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Review WHERE ProductID = ? ORDER BY ID";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, productID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ReviewDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID"; // Default
        }

        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Review ORDER BY " + order;
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
        return List.of("ID", "UserID", "ProductID", "Title", "Rating");
    }

    @Override
    protected void validate(ReviewDTO review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null.");
        }
        if (review.getUserID() <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer.");
        }
        if (review.getProductID() <= 0) {
            throw new IllegalArgumentException("ProductID must be a positive integer.");
        }
        if (review.getTitle() == null || review.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

    @Override
    protected ReviewDTO extract(ResultSet rs) throws SQLException {
        ReviewDTO review = new ReviewDTO();
        review.setId(rs.getInt("ID"));
        review.setUserID(rs.getInt("UserID"));
        review.setProductID(rs.getInt("ProductID"));
        review.setTitle(rs.getString("Title"));
        review.setDescription(rs.getString("Description"));
        review.setRating(rs.getInt("Rating")); // TINYINT in DB, int in Java
        return review;
    }
}