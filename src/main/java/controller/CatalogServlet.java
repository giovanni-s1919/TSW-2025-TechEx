package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import model.dto.UserDTO;
import model.dto.ProductDTO;
import model.dao.ProductDAO;

@WebServlet(name = "CatalogServlet", value = {"/catalog"})
public class CatalogServlet extends HttpServlet {
    private ProductDAO productDAO;
    private Gson gson = new Gson();

    public void init() throws ServletException {
        super.init();
        DataSource dataSource = (DataSource) getServletContext().getAttribute("datasource");
        if (dataSource == null) {
            throw new ServletException("DataSource non disponibile nel contesto della servlet.");
        }
        this.productDAO = new ProductDAO(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("filterProducts".equals(action)) {
            handleFilterProducts(request, response);
            return;
        }
        HttpSession session = request.getSession(false);
        String role = "Guest";
        if (session != null) {
            UserDTO user = (UserDTO) session.getAttribute("user");
            if (user != null) {
                role = user.getRole().name();
            }
        }
        request.setAttribute("role", role);
        try {
            List<ProductDTO> allProducts = productDAO.findAll("Name");
            request.setAttribute("products", allProducts);
            request.setAttribute("categories", ProductDTO.Category.values());
            request.setAttribute("grades", ProductDTO.Grade.values());
            List<String> brands = productDAO.findDistinctBrands();
            request.setAttribute("brands", brands);
        } catch (SQLException e) {
            log("Errore nel recuperare i dati per il catalogo", e);
            request.setAttribute("errorMessage", "Impossibile caricare il catalogo dei prodotti. Riprova più tardi.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
    }

    private void handleFilterProducts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String[] categories = request.getParameterValues("category");
            String[] brands = request.getParameterValues("brand");
            String[] grades = request.getParameterValues("grade");
            String priceRange = request.getParameter("price");
            String sortBy = request.getParameter("sort");
            List<String> categoryList = (categories != null) ? Arrays.asList(categories) : null;
            List<String> brandList = (brands != null) ? Arrays.asList(brands) : null;
            List<String> gradeList = (grades != null) ? Arrays.asList(grades) : null;
            List<ProductDTO> filteredProducts = productDAO.findByFilters(categoryList, brandList, gradeList, priceRange, sortBy);
            List<Map<String, Object>> productsForJson = new ArrayList<>();
            for (ProductDTO product : filteredProducts) {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getId());
                productMap.put("name", product.getName());
                productMap.put("price", product.getPrice());
                productMap.put("grade", product.getGrade().toString());
                productMap.put("category", product.getCategory().toString());
                productsForJson.add(productMap);
            }
            response.getWriter().write(gson.toJson(productsForJson));
        } catch (SQLException e) {
            log("Errore SQL durante il filtraggio dei prodotti", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Errore del server durante la ricerca.")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("filterProducts".equals(action)) {
            handleFilterProducts(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", "Azione POST non supportata.")));
        }
    }
}