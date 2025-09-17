package model.dao;

import model.dto.AddressDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO extends AbstractDAO<AddressDTO, Integer>{

    public AddressDAO(DataSource ds) {
        super(ds);
    }


    @Override
    public void save(AddressDTO address) throws SQLException {
        validate(address);

        String sql = "INSERT INTO Address " +
                     "(Street, AdditionalInfo, City, PostalCode, Region, Country, Name, Surname, Phone, AddressType) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getAdditionalInfo());
            ps.setString(3, address.getCity());
            ps.setString(4, address.getPostalCode());
            ps.setString(5, address.getRegion());
            ps.setString(6, address.getCountry());
            ps.setString(7, address.getName());
            ps.setString(8, address.getSurname());
            ps.setString(9, address.getPhone());
            ps.setString(10, address.getAddressType().name());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    address.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(AddressDTO address) throws SQLException {
        validate(address);
        if(address.getId() < 1) throw new IllegalArgumentException("Address ID must be positive for update operations.");

        String sql = "UPDATE Address SET Street=?, AdditionalInfo=?, City=?, PostalCode=?, Region=?, Country=?, Name=?, Surname=?, Phone=?, AddressType=? WHERE ID=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getAdditionalInfo());
            ps.setString(3, address.getCity());
            ps.setString(4, address.getPostalCode());
            ps.setString(5, address.getRegion());
            ps.setString(6, address.getCountry());
            ps.setString(7, address.getName());
            ps.setString(8, address.getSurname());
            ps.setString(9, address.getPhone());
            ps.setString(10, address.getAddressType().name());
            ps.setInt(11, address.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("ID must be a positive integer.");

        String sql = "DELETE FROM Address WHERE ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public AddressDTO findById(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("ID must be a positive integer.");

        String sql = "SELECT * FROM Address WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extract(rs);
                }
            }
        }

        return null;
    }

    public List<AddressDTO> findAddressesByUserId(int userId) throws SQLException {
        List<AddressDTO> addresses = new ArrayList<>();
        String sql = "SELECT a.*, ua.IsDefault FROM Address a " +
                "JOIN UserAddress ua ON a.ID = ua.AddressID " +
                "WHERE ua.UserID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AddressDTO address = extract(rs);
                    address.setDefault(rs.getBoolean("IsDefault"));
                    addresses.add(address);
                }
            }
        }
        return addresses;
    }

    @Override
    public List<AddressDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID";
        }

        List<AddressDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Address ORDER BY " + order;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery()){
            while(rs.next()){
                list.add(extract(rs));
            }
        }

        return list;
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        return List.of("ID", "Street", "AdditionalInfo", "City", "PostalCode", "Region", "Country", "Name", "Surname", "Phone", "AddressType");
    }


    @Override
    protected AddressDTO extract(ResultSet rs) throws SQLException {
        AddressDTO address = new AddressDTO();
        address.setId(rs.getInt("ID"));
        address.setStreet(rs.getString("Street"));
        address.setAdditionalInfo(rs.getString("AdditionalInfo"));
        address.setCity(rs.getString("City"));
        address.setPostalCode(rs.getString("PostalCode"));
        address.setRegion(rs.getString("Region"));
        address.setCountry(rs.getString("Country"));
        address.setName(rs.getString("Name"));
        address.setSurname(rs.getString("Surname"));
        address.setPhone(rs.getString("Phone"));
        address.setAddressType(AddressDTO.AddressType.valueOf(rs.getString("AddressType")));
        return address;
    }

    @Override
    protected void validate(AddressDTO address) {
        if (address == null ||
                address.getStreet() == null || address.getStreet().trim().isEmpty() ||
                address.getCity() == null || address.getCity().trim().isEmpty() ||
                address.getPostalCode() == null || address.getPostalCode().trim().isEmpty() ||
                address.getCountry() == null || address.getCountry().trim().isEmpty() ||
                address.getName() == null || address.getName().trim().isEmpty() ||
                address.getSurname() == null || address.getSurname().trim().isEmpty() ||
                address.getAddressType() == null) {
            throw new IllegalArgumentException("Some required address fields are null or empty.");
        }
        if (address.getPhone() != null && !address.getPhone().trim().isEmpty() &&
                (address.getPhone().length() > 15 || !address.getPhone().matches("^[0-9+\\- ]+$"))) {
            throw new IllegalArgumentException("Phone must be max 15 characters and contain only digits, spaces, '+' or '-'.");
        }
    }
}