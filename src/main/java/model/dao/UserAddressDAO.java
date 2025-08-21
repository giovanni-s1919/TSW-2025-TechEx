package model.dao;

import model.dto.UserAddressDTO;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAddressDAO extends AbstractDAO<UserAddressDTO, Integer> {

    public UserAddressDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(UserAddressDTO userAddress) throws SQLException {
        validate(userAddress);

        String sql = "INSERT INTO UserAddress (AddressID, UserID, IsDefault) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userAddress.getAddressId());
            ps.setInt(2, userAddress.getUserId());
            ps.setBoolean(3, userAddress.isDefault());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(UserAddressDTO userAddress) throws SQLException {
        validate(userAddress);
        if (userAddress.getAddressId() <= 0 || userAddress.getUserId() <= 0) {
            throw new IllegalArgumentException("Composite primary key (AddressID, UserID) must be valid for update operations.");
        }

        String sql = "UPDATE UserAddress SET IsDefault = ? WHERE AddressID = ? AND UserID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, userAddress.isDefault());
            ps.setInt(2, userAddress.getAddressId());
            ps.setInt(3, userAddress.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        // Questo metodo non è supportato direttamente per chiavi primarie composite.
        // Utilizzare il metodo delete(int addressId, int userId, UserAddressDTO.AddressType addressType)
        throw new UnsupportedOperationException("Deletion by single ID is not supported for UserAddress, use delete(addressId, userId, addressType) instead.");
    }

    public boolean delete(int addressId, int userId) throws SQLException {
        if (addressId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("Composite primary key (AddressID, UserID) must be valid for deletion.");
        }

        String sql = "DELETE FROM UserAddress WHERE AddressID = ? AND UserID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public UserAddressDTO findById(Integer id) throws SQLException {
        // Questo metodo non è supportato direttamente per chiavi primarie composite.
        // Utilizzare il metodo findById(int addressId, int userId, UserAddressDTO.AddressType addressType)
        throw new UnsupportedOperationException("Lookup by single ID is not supported for UserAddress, use findById(addressId, userId, addressType) instead.");
    }

    public UserAddressDTO findById(int addressId, int userId) throws SQLException {
        if (addressId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("Composite primary key (AddressID, UserID, AddressType) must be valid for lookup.");
        }

        String sql = "SELECT * FROM UserAddress WHERE AddressID = ? AND UserID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extract(rs);
                }
            }
        }
        return null;
    }

    // Metodo per trovare tutti gli indirizzi associati a un utente specifico
    public List<UserAddressDTO> findByUserID(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer.");
        }

        List<UserAddressDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM UserAddress WHERE UserID = ? ORDER BY AddressID";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    // Metodo per trovare tutti gli utenti associati a un indirizzo specifico
    public List<UserAddressDTO> findByAddressID(int addressId) throws SQLException {
        if (addressId <= 0) {
            throw new IllegalArgumentException("AddressID must be a positive integer.");
        }

        List<UserAddressDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM UserAddress WHERE AddressID = ? ORDER BY UserID";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, addressId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<UserAddressDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "AddressID, UserID";
        }

        List<UserAddressDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM UserAddress ORDER BY " + order;
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
        return List.of("AddressID", "UserID", "IsDefault");
    }

    @Override
    protected void validate(UserAddressDTO userAddress) {
        if (userAddress == null) {
            throw new IllegalArgumentException("UserAddress cannot be null.");
        }
        if (userAddress.getAddressId() <= 0) {
            throw new IllegalArgumentException("AddressID must be a positive integer.");
        }
        if (userAddress.getUserId() <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer.");
        }
        // isDefault è un boolean, non richiede validazione di nullità o vuoto.
    }

    @Override
    protected UserAddressDTO extract(ResultSet rs) throws SQLException {
        UserAddressDTO userAddress = new UserAddressDTO();
        userAddress.setAddressId(rs.getInt("AddressID"));
        userAddress.setUserId(rs.getInt("UserID"));
        userAddress.setDefault(rs.getBoolean("IsDefault"));
        return userAddress;
    }
}