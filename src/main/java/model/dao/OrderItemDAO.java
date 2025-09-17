package model.dao;

import model.dto.OrderItemDTO;
import model.dto.ProductDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO extends AbstractDAO<OrderItemDTO, Integer> {

    public OrderItemDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(OrderItemDTO orderItem) throws SQLException {
        validate(orderItem);

        String sql = "INSERT INTO OrderItem (OrderID, ItemName, ItemDescription, ItemBrand, ItemPrice, ItemCategory, ItemGrade, ItemQuantity, ItemVAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, orderItem.getOrderID());
            ps.setString(2, orderItem.getItemName());
            ps.setString(3, orderItem.getItemDescription());
            ps.setString(4, orderItem.getItemBrand());
            ps.setFloat(5, orderItem.getItemPrice());
            ps.setString(6, orderItem.getItemCategory().name());
            ps.setString(7, orderItem.getItemGrade().name());
            ps.setInt(8, orderItem.getItemQuantity());
            ps.setFloat(9, orderItem.getItemVAT());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderItem.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void save(OrderItemDTO orderItem, Connection connection) throws SQLException {
        validate(orderItem);
        String sql = "INSERT INTO OrderItem (OrderID, ItemName, ItemDescription, ItemBrand, ItemPrice, ItemCategory, ItemGrade, ItemQuantity, ItemVAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orderItem.getOrderID());
            ps.setString(2, orderItem.getItemName());
            ps.setString(3, orderItem.getItemDescription());
            ps.setString(4, orderItem.getItemBrand());
            ps.setFloat(5, orderItem.getItemPrice());
            ps.setString(6, orderItem.getItemCategory().name());
            ps.setString(7, orderItem.getItemGrade().name());
            ps.setInt(8, orderItem.getItemQuantity());
            ps.setFloat(9, orderItem.getItemVAT());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderItem.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(OrderItemDTO orderItem) throws SQLException {
        validate(orderItem);
        if (orderItem.getId() <= 0) {
            throw new IllegalArgumentException("OrderItem ID must be positive for update operations.");
        }

        String sql = "UPDATE OrderItem SET OrderID = ?, ItemName = ?, ItemDescription = ?, ItemBrand = ?, ItemPrice = ?, ItemCategory = ?, ItemGrade = ?, ItemQuantity = ?, ItemVAT = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderItem.getOrderID());
            ps.setString(2, orderItem.getItemName());
            ps.setString(3, orderItem.getItemDescription());
            ps.setString(4, orderItem.getItemBrand());
            ps.setFloat(5, orderItem.getItemPrice());
            ps.setString(6, orderItem.getItemCategory().name());
            ps.setString(7, orderItem.getItemGrade().name());
            ps.setInt(8, orderItem.getItemQuantity());
            ps.setFloat(9, orderItem.getItemVAT());
            ps.setInt(10, orderItem.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("OrderItem ID must be a positive integer for deletion.");
        }

        String sql = "DELETE FROM OrderItem WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public OrderItemDTO findById(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("OrderItem ID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM OrderItem WHERE ID = ?";
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

    public List<OrderItemDTO> findByOrderId(int orderId) throws SQLException {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be a positive integer.");
        }

        List<OrderItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderItem WHERE OrderID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<OrderItemDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID";
        }

        List<OrderItemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderItem ORDER BY " + order;
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
        return List.of("ID", "OrderID", "ItemName", "ItemDescription", "ItemBrand", "ItemPrice", "ItemCategory", "ItemGrade", "ItemQuantity", "ItemVAT");
    }


    @Override
    protected void validate(OrderItemDTO orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("OrderItem cannot be null.");
        }
        if (orderItem.getOrderID() <= 0) {
            throw new IllegalArgumentException("OrderID must be a positive integer.");
        }
        if (orderItem.getItemName() == null || orderItem.getItemName().trim().isEmpty()) {
            throw new IllegalArgumentException("ItemName cannot be null or empty.");
        }
         if (orderItem.getItemBrand() == null || orderItem.getItemBrand().trim().isEmpty()) {
             throw new IllegalArgumentException("ItemBrand cannot be null or empty.");
         }
         if (orderItem.getItemCategory() == null) {
             throw new IllegalArgumentException("ItemCategory cannot be null or empty.");
         }
         if (orderItem.getItemGrade() == null) {
             throw new IllegalArgumentException("ItemGrade cannot be null or empty.");
         }
        if (orderItem.getItemPrice() < 0) {
            throw new IllegalArgumentException("ItemPrice cannot be negative.");
        }
        if (orderItem.getItemQuantity() <= 0) {
            throw new IllegalArgumentException("ItemQuantity must be a positive integer.");
        }
        if (orderItem.getItemVAT() < 0) {
            throw new IllegalArgumentException("ItemVAT cannot be negative.");
        }
    }

    @Override
    protected OrderItemDTO extract(ResultSet rs) throws SQLException {
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setId(rs.getInt("ID"));
        orderItem.setOrderID(rs.getInt("OrderID"));
        orderItem.setItemName(rs.getString("ItemName"));
        orderItem.setItemDescription(rs.getString("ItemDescription"));
        orderItem.setItemBrand(rs.getString("ItemBrand"));
        orderItem.setItemPrice(rs.getFloat("ItemPrice"));
        orderItem.setItemCategory(OrderItemDTO.Category.valueOf(rs.getString("ItemCategory")));
        orderItem.setItemGrade(OrderItemDTO.Grade.valueOf(rs.getString("ItemGrade")));
        orderItem.setItemQuantity(rs.getInt("ItemQuantity"));
        orderItem.setItemVAT(rs.getFloat("ItemVAT"));
        return orderItem;
    }
}
