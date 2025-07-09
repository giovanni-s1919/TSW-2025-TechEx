package model.dao;

import model.dto.PaymentMethodDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDAO extends AbstractDAO<PaymentMethodDTO, Integer> {

    public PaymentMethodDAO(DataSource ds) {
        super(ds);
    }

    @Override
    public void save(PaymentMethodDTO paymentMethod) throws SQLException {
        validate(paymentMethod);

        String sql = "INSERT INTO PaymentMethod (UserID, Number, Expiration, Name, IsDefault) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, paymentMethod.getUserID());
            ps.setString(2, paymentMethod.getNumber());
            ps.setDate(3, Date.valueOf(paymentMethod.getExpiration())); // Salva LocalDate come java.sql.Date
            ps.setString(4, paymentMethod.getName());
            ps.setBoolean(5, paymentMethod.isDefault());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paymentMethod.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(PaymentMethodDTO paymentMethod) throws SQLException {
        validate(paymentMethod);
        if (paymentMethod.getId() <= 0) {
            throw new IllegalArgumentException("PaymentMethod ID must be positive for update operations.");
        }

        String sql = "UPDATE PaymentMethod SET UserID = ?, Number = ?, Expiration = ?, Name = ?, IsDefault = ? WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, paymentMethod.getUserID());
            ps.setString(2, paymentMethod.getNumber());
            ps.setDate(3, Date.valueOf(paymentMethod.getExpiration()));
            ps.setString(4, paymentMethod.getName());
            ps.setBoolean(5, paymentMethod.isDefault());
            ps.setInt(6, paymentMethod.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("PaymentMethod ID must be a positive integer for deletion.");
        }

        String sql = "DELETE FROM PaymentMethod WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public PaymentMethodDTO findById(Integer id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("PaymentMethod ID must be a positive integer for lookup.");
        }

        String sql = "SELECT * FROM PaymentMethod WHERE ID = ?";
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

    // Metodo per trovare i metodi di pagamento di un utente specifico
    public List<PaymentMethodDTO> findByUserID(int userID) throws SQLException {
        if (userID <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer.");
        }

        List<PaymentMethodDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethod WHERE UserID = ? ORDER BY ID"; // Ordinamento di default per ID
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


    @Override
    public List<PaymentMethodDTO> findAll(String order) throws SQLException {
        if (!getAllowedOrderColumns().contains(order)) {
            order = "ID"; // Default
        }

        List<PaymentMethodDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethod ORDER BY " + order;
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
        return List.of("ID", "UserID", "Number", "Expiration", "Name", "IsDefault");
    }

    @Override
    protected void validate(PaymentMethodDTO paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("PaymentMethod cannot be null.");
        }
        if (paymentMethod.getUserID() <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer.");
        }
        if (paymentMethod.getNumber() == null || paymentMethod.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Number cannot be null or empty.");
        }
        // La lunghezza del numero è VARCHAR(4) nel DB, quindi un controllo aggiuntivo può essere utile
        if (paymentMethod.getNumber().length() != 4) {
            throw new IllegalArgumentException("Number must be exactly 4 characters long.");
        }
        if (paymentMethod.getExpiration() == null) {
            throw new IllegalArgumentException("Expiration date cannot be null.");
        }
        // Potresti voler aggiungere una validazione per assicurarti che la data di scadenza sia nel futuro
        if (paymentMethod.getExpiration().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiration date cannot be in the past.");
        }
        if (paymentMethod.getName() == null || paymentMethod.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        // isDefault è un boolean, non richiede validazione di nullità o vuoto.
    }

    @Override
    protected PaymentMethodDTO extract(ResultSet rs) throws SQLException {
        PaymentMethodDTO paymentMethod = new PaymentMethodDTO();
        paymentMethod.setId(rs.getInt("ID"));
        paymentMethod.setUserID(rs.getInt("UserID"));
        paymentMethod.setNumber(rs.getString("Number"));
        // Recupera java.sql.Date e lo converte in LocalDate
        Date expirationSql = rs.getDate("Expiration");
        paymentMethod.setExpiration(expirationSql != null ? expirationSql.toLocalDate() : null);
        paymentMethod.setName(rs.getString("Name"));
        paymentMethod.setDefault(rs.getBoolean("IsDefault"));
        return paymentMethod;
    }
}
