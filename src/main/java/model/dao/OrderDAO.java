package model.dao;

import model.dto.OrderDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends AbstractDAO<OrderDTO, Integer>{

    public OrderDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(OrderDTO order) throws SQLException {
        validate(order);

        String sql = "INSERT INTO `Order` (UserID, OrderDate, OrderStatus, DeliveryDate, TotalAmount, ShippingAddressId, BillingAddressId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserID());
            ps.setTimestamp(2, order.getOrderDate()); // Salva Timestamp
            ps.setString(3, order.getOrderStatus());
            ps.setDate(4, Date.valueOf(order.getDeliveryDate())); // Salva LocalDate come java.sql.Date
            ps.setFloat(5, order.getTotalAmount());
            ps.setInt(6, order.getShippingAddressId());
            ps.setInt(7, order.getBillingAddressId());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(OrderDTO order) throws SQLException {
        validate(order);
        if (order.getId() <= 0) {
            throw new IllegalArgumentException("Order ID must be positive for update operations.");
        }

        String sql = "UPDATE `Order` SET UserID = ?, OrderDate = ?, OrderStatus = ?, DeliveryDate = ?, TotalAmount = ?, ShippingAddressId = ?, BillingAddressId = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, order.getUserID());
            ps.setTimestamp(2, order.getOrderDate());
            ps.setString(3, order.getOrderStatus());
            ps.setDate(4, Date.valueOf(order.getDeliveryDate()));
            ps.setFloat(5, order.getTotalAmount());
            ps.setInt(6, order.getShippingAddressId());
            ps.setInt(7, order.getBillingAddressId());
            ps.setInt(8, order.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Order ID must be a positive integer for deletion.");
        }

        String sql = "DELETE FROM `Order` WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public OrderDTO findById(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Order ID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM `Order` WHERE ID = ?";
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
    public List<OrderDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID"; // Default
        }

        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM `Order` ORDER BY " + order;
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
        return List.of("ID", "UserID", "OrderDate", "OrderStatus", "DeliveryDate", "TotalAmount", "ShippingAddressId", "BillingAddressId");
    }


    @Override
    protected void validate(OrderDTO order) {
        if(order == null ||
        order.getUserID() <= 0 ||
        order.getOrderDate() == null ||
        order.getOrderStatus() == null || order.getOrderStatus().trim().isEmpty() ||
        order.getTotalAmount() < 0 ||
        order.getShippingAddressId() <= 0 || order.getBillingAddressId() <= 0)
            throw new IllegalArgumentException("Some required order fields are null or empty.");
    }

    @Override
    protected OrderDTO extract(ResultSet rs) throws SQLException {
        OrderDTO order = new OrderDTO();
        order.setId(rs.getInt("ID"));
        order.setUserID(rs.getInt("UserID"));
        order.setOrderDate(rs.getTimestamp("OrderDate")); // Recupera Timestamp
        order.setOrderStatus(rs.getString("OrderStatus"));
        Date deliveryDateSql = rs.getDate("DeliveryDate");
        order.setDeliveryDate(deliveryDateSql != null ? deliveryDateSql.toLocalDate() : null);
        order.setTotalAmount(rs.getFloat("TotalAmount"));
        order.setShippingAddressId(rs.getInt("ShippingAddressId"));
        order.setBillingAddressId(rs.getInt("BillingAddressId"));
        return order;
    }
}
