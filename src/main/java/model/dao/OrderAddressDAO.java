package model.dao;

import model.dto.OrderAddressDTO;

import java.sql.SQLException;
import java.util.List;

public class OrderAddressDAO implements GenericDAO<OrderAddressDTO, Integer>{
    @Override
    public void save(OrderAddressDTO entity) throws SQLException {

    }

    @Override
    public void update(OrderAddressDTO entity) throws SQLException {

    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        return false;
    }

    @Override
    public OrderAddressDTO findById(Integer integer) throws SQLException {
        return null;
    }

    @Override
    public List<OrderAddressDTO> findAll(String order) throws SQLException {
        return List.of();
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        return List.of();
    }
}
