package model.dao;

import model.dto.OrderAddressDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderAddressDAO extends AbstractDAO<OrderAddressDTO, Integer>{

    public OrderAddressDAO(DataSource ds) {
        super(ds);
    }


    @Override
    public void save(OrderAddressDTO orderAddress) throws SQLException {
        validate(orderAddress);

        String sql = "INSERT INTO OrderAddress (Street, City, PostalCode, Region, Country, Name, Surname, Phone, AddressType) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, orderAddress.getStreet());
            ps.setString(2, orderAddress.getCity());
            ps.setString(3, orderAddress.getPostalCode());
            ps.setString(4, orderAddress.getRegion());
            ps.setString(5, orderAddress.getCountry());
            ps.setString(6, orderAddress.getName());
            ps.setString(7, orderAddress.getSurname());
            ps.setString(8, orderAddress.getPhone());
            ps.setString(9, orderAddress.getAddressType().name()); // Salva l'enum come String
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderAddress.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void save(OrderAddressDTO orderAddress, Connection connection) throws SQLException {
        validate(orderAddress);
        String sql = "INSERT INTO OrderAddress (Street, City, PostalCode, Region, Country, Name, Surname, Phone, AddressType) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; // Usa la connessione passata, NON ne apre una nuova
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, orderAddress.getStreet());
            ps.setString(2, orderAddress.getCity());
            ps.setString(3, orderAddress.getPostalCode());
            ps.setString(4, orderAddress.getRegion());
            ps.setString(5, orderAddress.getCountry());
            ps.setString(6, orderAddress.getName());
            ps.setString(7, orderAddress.getSurname());
            ps.setString(8, orderAddress.getPhone());
            ps.setString(9, orderAddress.getAddressType().name());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderAddress.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(OrderAddressDTO orderAddress) throws SQLException {
        validate(orderAddress);
        if (orderAddress.getId() < 1) {
            throw new IllegalArgumentException("OrderAddress ID must be positive for update operations.");
        }

        String sql = "UPDATE OrderAddress SET Street = ?, City = ?, PostalCode = ?, Region = ?, Country = ?, Name = ?, Surname = ?, Phone = ?, AddressType = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderAddress.getStreet());
            ps.setString(2, orderAddress.getCity());
            ps.setString(3, orderAddress.getPostalCode());
            ps.setString(4, orderAddress.getRegion());
            ps.setString(5, orderAddress.getCountry());
            ps.setString(6, orderAddress.getName());
            ps.setString(7, orderAddress.getSurname());
            ps.setString(8, orderAddress.getPhone());
            ps.setString(9, orderAddress.getAddressType().name()); // Aggiorna l'enum come String
            ps.setInt(10, orderAddress.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("OrderAddress ID must be a positive integer for deletion.");
        }

        String sql = "DELETE FROM OrderAddress WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public OrderAddressDTO findById(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("OrderAddress ID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM OrderAddress WHERE ID = ?";
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
    public List<OrderAddressDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID"; // Default
        }

        List<OrderAddressDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderAddress ORDER BY " + order;
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
        return List.of("ID", "Street", "City", "PostalCode", "Region", "Country", "Name", "Surname", "Phone", "AddressType");
    }


    @Override
    protected void validate(OrderAddressDTO orderAddress) {
        if (orderAddress == null ||
                orderAddress.getStreet() == null || orderAddress.getStreet().trim().isEmpty() ||
                orderAddress.getCity() == null || orderAddress.getCity().trim().isEmpty() ||
                orderAddress.getPostalCode() == null || orderAddress.getPostalCode().trim().isEmpty() ||
                orderAddress.getRegion() == null || orderAddress.getRegion().trim().isEmpty() ||
                orderAddress.getCountry() == null || orderAddress.getCountry().trim().isEmpty() ||
                orderAddress.getName() == null || orderAddress.getName().trim().isEmpty() ||
                orderAddress.getSurname() == null || orderAddress.getSurname().trim().isEmpty() ||
                orderAddress.getAddressType() == null)
            throw new IllegalArgumentException("Some required address fields are null or empty.");
        if (orderAddress.getPhone() != null &&
                (orderAddress.getPhone().length() > 15 || !orderAddress.getPhone().matches("^[0-9+\\- ]+$"))) {
            throw new IllegalArgumentException("Phone must be max 15 characters and contain only digits, spaces, '+' or '-'.");
        }
    }

    @Override
    protected OrderAddressDTO extract(ResultSet rs) throws SQLException {
        OrderAddressDTO orderAddress = new OrderAddressDTO();
        orderAddress.setId(rs.getInt("ID"));
        orderAddress.setStreet(rs.getString("Street"));
        orderAddress.setCity(rs.getString("City"));
        orderAddress.setPostalCode(rs.getString("PostalCode"));
        orderAddress.setRegion(rs.getString("Region"));
        orderAddress.setCountry(rs.getString("Country"));
        orderAddress.setName(rs.getString("Name"));
        orderAddress.setSurname(rs.getString("Surname"));
        orderAddress.setPhone(rs.getString("Phone"));
        orderAddress.setAddressType(OrderAddressDTO.AddressType.valueOf(rs.getString("AddressType")));
        return orderAddress;
    }
}

