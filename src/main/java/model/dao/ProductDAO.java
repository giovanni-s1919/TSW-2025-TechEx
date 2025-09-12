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

        String sql = "INSERT INTO Product (Name, Description, Brand, Price, Category, Grade, StockQuantity, VAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getBrand());
            ps.setFloat(4, product.getPrice()); // DECIMAL(10, 2) in DB, float in Java
            ps.setString(5, product.getCategory().name());
            ps.setString(6, product.getGrade().name());
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
        String sql = "UPDATE Product SET Name = ?, Description = ?, Brand = ?, Price = ?, Category = ?, Grade = ?, StockQuantity = ?, VAT = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getBrand());
            ps.setFloat(4, product.getPrice());
            ps.setString(5, product.getCategory().name());
            ps.setString(6, product.getGrade().name());
            ps.setInt(7, product.getStockQuantity());
            ps.setFloat(8, product.getVat());
            ps.setInt(9, product.getId());
            ps.executeUpdate();
        }
    }

    public void update(ProductDTO product, Connection connection) throws SQLException {
        validate(product);
        if (product.getId() <= 0) {
            throw new IllegalArgumentException("Product ID must be positive for update operations.");
        }
        String sql = "UPDATE Product SET Name = ?, Description = ?, Brand = ?, Price = ?, Category = ?, Grade = ?, StockQuantity = ?, VAT = ? WHERE ID = ?"; // Usa la connessione passata
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getBrand());
            ps.setFloat(4, product.getPrice());
            ps.setString(5, product.getCategory().name());
            ps.setString(6, product.getGrade().name());
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
            System.out.println("ERRORE PARAMETRO");
            throw new IllegalArgumentException("Product ID must be a positive integer for lookup.");
        }
        System.out.println("Primo throw superato");
        String sql = "SELECT * FROM Product WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            System.out.println("Connessione effettuata");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("rs.next() effettuato");
                    ProductDTO product = new ProductDTO();
                    System.out.println("Prodotto inizializzato");
                    product.setId(rs.getInt("ID"));
                    System.out.println("ID impostato");
                    product.setName(rs.getString("Name"));
                    System.out.println("Nome impostato");
                    product.setDescription(rs.getString("Description"));
                    System.out.println("Descrizione impostata");
                    product.setBrand(rs.getString("Brand"));
                    System.out.println("Brand impostato");
                    product.setPrice(rs.getFloat("Price"));
                    System.out.println("Prezzo impostato");
                    product.setCategory(ProductDTO.Category.valueOf(rs.getString("Category")));
                    System.out.println("Categoria impostato");
                    product.setGrade(ProductDTO.Grade.valueOf(rs.getString("Grade")));
                    System.out.println("Grado impostato");
                    product.setStockQuantity(rs.getInt("StockQuantity"));
                    System.out.println("StockQuantity impostato");
                    System.out.println("Operazione completata");
                    return product;
                }
            }
        }
        System.out.println("Operazione completata, prodotto restituito NULL");
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

    // Metodo per trovare prodotti per grado
    public List<ProductDTO> findByGrade(ProductDTO.Grade grade) throws SQLException {
        if (grade == null) {
            throw new IllegalArgumentException("Grade cannot be null.");
        }
        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE Grade = ? ORDER BY Name";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, grade.name());
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

    public List<ProductDTO> findByFilters(List<String> categories, List<String> brands, List<String> grades, String priceRange, String sortBy) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (categories != null && !categories.isEmpty()) {
            sql.append(" AND Category IN (");
            for (int i = 0; i < categories.size(); i++) {
                sql.append(i == 0 ? "?" : ", ?");
            }
            sql.append(")");
            params.addAll(categories);
        }
        if (brands != null && !brands.isEmpty()) {
            sql.append(" AND (");
            for (int i = 0; i < brands.size(); i++) {
                sql.append(i == 0 ? "UPPER(Name) LIKE ?" : " OR UPPER(Name) LIKE ?");
            }
            sql.append(")");
            for (String brand : brands) {
                params.add("%" + brand.toUpperCase() + "%");
            }
        }
        if (grades != null && !grades.isEmpty()) {
            sql.append(" AND Grade IN (");
            for (int i = 0; i < grades.size(); i++) {
                sql.append(i == 0 ? "?" : ", ?");
            }
            sql.append(")");
            params.addAll(grades);
        }
        if (priceRange != null && !priceRange.equals("any")) {
            String[] prices = priceRange.split("-");
            if (prices.length == 2) {
                sql.append(" AND Price BETWEEN ? AND ?");
                params.add(Float.parseFloat(prices[0]));
                params.add(Float.parseFloat(prices[1]));
            } else if (priceRange.endsWith("+")) {
                sql.append(" AND Price >= ?");
                params.add(Float.parseFloat(priceRange.replace("+", "")));
            }
        }
        if (sortBy != null && !sortBy.equals("default")) {
            if (sortBy.equals("price-asc")) {
                sql.append(" ORDER BY Price ASC");
            } else if (sortBy.equals("price-desc")) {
                sql.append(" ORDER BY Price DESC");
            }
        } else {
            sql.append(" ORDER BY Name ASC");
        }
        List<ProductDTO> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    // Metodo per ottenere la lista di Brand unici dal DB
    public List<String> findDistinctBrands() throws SQLException {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT DISTINCT Brand FROM Product ORDER BY Brand ASC";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                brands.add(rs.getString("Brand"));
            }
        }
        return brands;
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        return List.of("ID", "Name", "Brand", "Price", "Category", "Grade", "StockQuantity", "VAT");
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
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        if (product.getGrade() == null) {
            throw new IllegalArgumentException("Grade cannot be null or empty.");
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
        product.setCategory(ProductDTO.Category.valueOf(rs.getString("Category")));
        product.setGrade(ProductDTO.Grade.valueOf(rs.getString("Grade")));
        product.setStockQuantity(rs.getInt("StockQuantity"));
        product.setVat(rs.getFloat("VAT")); // DECIMAL(5, 2) in DB, float in Java
        return product;
    }
}