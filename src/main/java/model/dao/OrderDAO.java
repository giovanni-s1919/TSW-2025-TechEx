package model.dao;

import model.dto.OrderDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            ps.setTimestamp(2, order.getOrderDate());
            ps.setString(3, order.getOrderStatus().name());
            ps.setDate(4, Date.valueOf(order.getDeliveryDate()));
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

    public void save(OrderDTO order, Connection connection) throws SQLException {
        validate(order);
        String sql = "INSERT INTO `Order` (UserID, OrderDate, OrderStatus, DeliveryDate, TotalAmount, ShippingAddressId, BillingAddressId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserID());
            ps.setTimestamp(2, order.getOrderDate());
            ps.setString(3, order.getOrderStatus().name());
            if (order.getDeliveryDate() != null) {
                ps.setDate(4, Date.valueOf(order.getDeliveryDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }
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
            ps.setString(3, order.getOrderStatus().name());
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

    public List<OrderDTO> findByUserId(int userId) throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        String sql = "SELECT * FROM `Order` WHERE UserID = ? ORDER BY OrderDate DESC";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(extract(rs));
                }
            }
        }
        return orders;
    }

    public List<OrderDTO> findWithFilters(LocalDate startDate, LocalDate endDate, Integer customerId) throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM `Order` WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (startDate != null) {
            sql.append(" AND OrderDate >= ?");
            params.add(Timestamp.valueOf(startDate.atStartOfDay()));
        }
        if (endDate != null) {
            sql.append(" AND OrderDate <= ?");
            params.add(Timestamp.valueOf(endDate.atTime(23, 59, 59)));
        }
        if (customerId != null && customerId > 0) {
            sql.append(" AND UserID = ?");
            params.add(customerId);
        }

        sql.append(" ORDER BY OrderDate DESC");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(extract(rs));
                }
            }
        }
        return orders;
    }

    @Override
    public List<OrderDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID";
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
        order.getOrderStatus() == null ||
        order.getTotalAmount() < 0 ||
        order.getShippingAddressId() <= 0 || order.getBillingAddressId() <= 0)
            throw new IllegalArgumentException("Some required order fields are null or empty.");
    }

    @Override
    protected OrderDTO extract(ResultSet rs) throws SQLException {
        OrderDTO order = new OrderDTO();
        order.setId(rs.getInt("ID"));
        order.setUserID(rs.getInt("UserID"));
        order.setOrderDate(rs.getTimestamp("OrderDate"));
        order.setOrderStatus(OrderDTO.OrderStatus.valueOf(rs.getString("OrderStatus")));
        Date deliveryDateSql = rs.getDate("DeliveryDate");
        order.setDeliveryDate(deliveryDateSql != null ? deliveryDateSql.toLocalDate() : null);
        order.setTotalAmount(rs.getFloat("TotalAmount"));
        order.setShippingAddressId(rs.getInt("ShippingAddressId"));
        order.setBillingAddressId(rs.getInt("BillingAddressId"));
        return order;
    }

    public void updateOrderStatusByAge() throws SQLException {
        String updatePendingToProcessing = "UPDATE `Order` SET OrderStatus = ? WHERE OrderStatus = ? AND OrderDate <= ?";
        String updateProcessingToShipped = "UPDATE `Order` SET OrderStatus = ? WHERE OrderStatus = ? AND OrderDate <= ?";
        String updateShippedToDelivered = "UPDATE `Order` SET OrderStatus = ? WHERE OrderStatus = ? AND OrderDate <= ?";

        // Definiamo le soglie di tempo
        LocalDateTime pendingThreshold = LocalDateTime.now().minusMinutes(5);
        LocalDateTime processingThreshold = LocalDateTime.now().minusMinutes(10); // 5 min (pending) + 5 min (processing)
        LocalDateTime shippedThreshold = LocalDateTime.now().minusMinutes(15); // 10 min (precedenti) + 5 min (shipped)

        try (Connection connection = dataSource.getConnection()) {
            // Disabilita l'autocommit per eseguire tutte le query in una singola transazione
            connection.setAutoCommit(false);

            try (PreparedStatement psPending = connection.prepareStatement(updatePendingToProcessing);
                 PreparedStatement psProcessing = connection.prepareStatement(updateProcessingToShipped);
                 PreparedStatement psShipped = connection.prepareStatement(updateShippedToDelivered)) {

                // Da Pending -> a Processing dopo 5 minuti
                psPending.setString(1, OrderDTO.OrderStatus.Processing.name());
                psPending.setString(2, OrderDTO.OrderStatus.Pending.name());
                psPending.setTimestamp(3, Timestamp.valueOf(pendingThreshold));
                psPending.executeUpdate();

                // Da Processing -> a Shipped dopo altri 5 minuti (10 totali)
                psProcessing.setString(1, OrderDTO.OrderStatus.Shipped.name());
                psProcessing.setString(2, OrderDTO.OrderStatus.Processing.name());
                psProcessing.setTimestamp(3, Timestamp.valueOf(processingThreshold));
                psProcessing.executeUpdate();

                // Da Shipped -> a Delivered dopo altri 5 minuti (15 totali)
                psShipped.setString(1, OrderDTO.OrderStatus.Delivered.name());
                psShipped.setString(2, OrderDTO.OrderStatus.Shipped.name());
                psShipped.setTimestamp(3, Timestamp.valueOf(shippedThreshold));
                psShipped.executeUpdate();

                // Conferma le modifiche
                connection.commit();

            } catch (SQLException e) {
                // In caso di errore, annulla tutte le modifiche
                connection.rollback();
                throw e; // Rilancia l'eccezione per il logging
            } finally {
                // Riabilita l'autocommit
                connection.setAutoCommit(true);
            }
        }
    }
}
