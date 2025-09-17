package util;

import jakarta.servlet.http.HttpServletRequest;
import model.dao.ProductDAO;
import model.dto.ProductDTO;

import java.sql.SQLException;

public class HeaderDataHelper {

    public static void loadHeaderData(HttpServletRequest request, ProductDAO productDAO) throws SQLException {
        request.setAttribute("categoriesForHeader", ProductDTO.Category.values());
        request.setAttribute("gradesForHeader", ProductDTO.Grade.values());
    }
}
