package model.dao;

import model.dto.ProductDTO;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends AbstractDAO<ProductDTO, Integer> {

    public ProductDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(ProductDTO product) throws SQLException {
        validate(product);

        String sql = "INSERT INTO Product (Name, Description, Brand, Price, Category, Seller, StockQuantity, VAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getBrand());
            ps.setFloat(4, product.getPrice()); // DECIMAL(10, 2) in DB, float in Java
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getSeller());
            ps.setInt(7, product.getStockQuantity());
            ps.setFloat(8, product.getVat()); // DECIMAL(5, 2) in DB, float in Java

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(ProductDTO product) throws SQLException {
        validate(product);
        if (product.getId() <= 0) {
            throw new IllegalArgumentException("Product ID must be positive for update operations.");
        }

        String sql = "UPDATE Product SET Name = ?, Description = ?, Brand = ?, Price = ?, Category = ?, Seller = ?, StockQuantity = ?, VAT = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getBrand());
            ps.setFloat(4, product.getPrice());
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getSeller());
            ps.setInt(7, product.getStockQuantity());
            ps.setFloat(8, product.getVat());
            ps.setInt(9, product.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive integer for deletion.");
        }

        String sql = "DELETE FROM Product WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public ProductDTO findById(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM Product WHERE ID = ?";
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

    // Metodo per trovare prodotti per categoria
    public List<ProductDTO> findByCategory(String category) throws SQLException {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }

        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE Category = ? ORDER BY Name"; // Ordinamento di default per nome
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    // Metodo per trovare prodotti per venditore
    public List<ProductDTO> findBySeller(String seller) throws SQLException {
        if (seller == null || seller.trim().isEmpty()) {
            throw new IllegalArgumentException("Seller cannot be null or empty.");
        }

        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE Seller = ? ORDER BY Name"; // Ordinamento di default per nome
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, seller);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<ProductDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID"; // Default
        }

        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Product ORDER BY " + order;
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
        return List.of("ID", "Name", "Brand", "Price", "Category", "Seller", "StockQuantity", "VAT");
    }

    @Override
    protected void validate(ProductDTO product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        // Description è TEXT e può essere null/vuota nel DB, quindi non la valido come obbligatoria qui
        if (product.getBrand() == null || product.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be null or empty.");
        }
        if (product.getPrice() < 0) { // Il prezzo non può essere negativo
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        if (product.getSeller() == null || product.getSeller().trim().isEmpty()) {
            throw new IllegalArgumentException("Seller cannot be null or empty.");
        }
        if (product.getStockQuantity() < 0) { // La quantità in stock non può essere negativa (CHECK nel DB)
            throw new IllegalArgumentException("StockQuantity cannot be negative.");
        }
        // VAT ha un DEFAULT nel DB, ma è NOT NULL, quindi deve essere valida
        if (product.getVat() < 0) { // L'IVA non può essere negativa
            throw new IllegalArgumentException("VAT cannot be negative.");
        }
    }

    @Override
    protected ProductDTO extract(ResultSet rs) throws SQLException {
        ProductDTO product = new ProductDTO();
        product.setId(rs.getInt("ID"));
        product.setName(rs.getString("Name"));
        product.setDescription(rs.getString("Description"));
        product.setBrand(rs.getString("Brand"));
        product.setPrice(rs.getFloat("Price")); // DECIMAL(10, 2) in DB, float in Java
        product.setCategory(rs.getString("Category"));
        product.setSeller(rs.getString("Seller"));
        product.setStockQuantity(rs.getInt("StockQuantity"));
        product.setVat(rs.getFloat("VAT")); // DECIMAL(5, 2) in DB, float in Java
        return product;
    }
}