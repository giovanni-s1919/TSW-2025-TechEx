package model.dao;

import model.dto.AddressDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO implements GenericDAO<AddressDTO, Integer>{
    private final DataSource dataSource;

    public AddressDAO(DataSource ds) {
        if(ds == null) throw new IllegalArgumentException("DataSource cannot be null.");
        this.dataSource = ds;
    }

    @Override
    public void save(AddressDTO address) throws SQLException {
        validateAddress(address);

        String sql = "INSERT INTO Address (Street, AdditionalInfo, City, PostalCode, Region, Country) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getAdditionalInfo());
            ps.setString(3, address.getCity());
            ps.setString(4, address.getPostalCode());
            ps.setString(5, address.getRegion());
            ps.setString(6, address.getCountry());

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
        validateAddress(address);

        String sql = "UPDATE Address SET Street = ?, AdditionalInfo = ?, City = ?, PostalCode = ?, Region = ?, Country = ? " +
                "WHERE ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address.getStreet());
            ps.setString(2, address.getAdditionalInfo());
            ps.setString(3, address.getCity());
            ps.setString(4, address.getPostalCode());
            ps.setString(5, address.getRegion());
            ps.setString(6, address.getCountry());
            ps.setInt(7, address.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("AddressID must be a positive integer.");

        String sql = "DELETE FROM Address WHERE ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public AddressDTO findById(Integer id) throws SQLException {
        if(id == null || id < 1)  throw new IllegalArgumentException("AddressID must be a positive integer.");

        String sql = "SELECT * FROM Address WHERE ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAddress(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<AddressDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID";
        }

        List<AddressDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Address WHERE " + order;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery()){
            while(rs.next()){
                list.add(extractAddress(rs));
            }
        }

        return list;
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        return List.of("ID", "Street", "AdditionalInfo", "City", "PostalCode", "Region", "Country");
    }


    private AddressDTO extractAddress(ResultSet rs) throws SQLException {
        AddressDTO address = new AddressDTO();
        address.setId(rs.getInt("ID"));
        address.setStreet(rs.getString("Street"));
        address.setAdditionalInfo(rs.getString("AdditionalInfo"));
        address.setCity(rs.getString("City"));
        address.setPostalCode(rs.getString("PostalCode"));
        address.setRegion(rs.getString("Region"));
        address.setCountry(rs.getString("Country"));
        return address;
    }

    private void validateAddress(AddressDTO address) {
        if (address == null ||
                address.getStreet() == null || address.getStreet().trim().isEmpty() ||
                address.getCity() == null || address.getCity().trim().isEmpty() ||
                address.getPostalCode() == null || address.getPostalCode().trim().isEmpty() ||
                address.getCountry() == null || address.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Some required address fields are null or empty.");
        }
    }
}
