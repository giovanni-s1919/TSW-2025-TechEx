package util;

import jakarta.servlet.http.HttpServletRequest;
import model.dao.ProductDAO;
import model.dto.ProductDTO;

import java.sql.SQLException;

public class HeaderDataHelper {
    /**
     * Carica i dati necessari per popolare i menu dinamici dell'header
     * e li imposta come attributi della richiesta.
     * @param request L'oggetto HttpServletRequest su cui impostare gli attributi.
     * @param productDAO Un'istanza di ProductDAO per accedere al database.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
    public static void loadHeaderData(HttpServletRequest request, ProductDAO productDAO) throws SQLException {
        request.setAttribute("categoriesForHeader", ProductDTO.Category.values());
        request.setAttribute("gradesForHeader", ProductDTO.Grade.values());
    }
}
