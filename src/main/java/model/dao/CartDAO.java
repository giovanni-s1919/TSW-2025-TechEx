package model.dao;

import model.dto.CartDTO;

import java.sql.SQLException;
import java.util.List;

public class CartDAO implements GenericDAO<CartDTO, Integer>{

    @Override
    public void save(CartDTO entity) throws SQLException {

    }

    @Override
    public void update(CartDTO entity) throws SQLException {

    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        return false;
    }

    @Override
    public CartDTO findById(Integer integer) throws SQLException {
        return null;
    }

    @Override
    public List<CartDTO> findAll(String order) throws SQLException {
        return List.of();
    }

    @Override
    public List<String> getAllowedOrderColumns() {
        return List.of();
    }
}
