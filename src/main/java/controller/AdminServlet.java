package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.OrderDAO;
import model.dao.ProductDAO;
import model.dto.ProductDTO;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


// Mettiamo la servlet sotto il percorso /admin/ per farla intercettare dal filtro
@WebServlet(name = "AdminServlet", value = {"/admin/panel"})
public class AdminServlet extends HttpServlet {
    // Inizializza TUTTI i DAO di cui avrai bisogno (ProductDAO, OrderDAO, UserDAO...)
    private ProductDAO productDAO;
    private OrderDAO orderDAO; // Assumendo che esista
    private Gson gson = new Gson();

    @Override
    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        this.productDAO = new ProductDAO(ds);
        this.orderDAO = new OrderDAO(ds);
    }

    // Il doGet carica semplicemente la pagina JSP
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/admin_panel.jsp").forward(request, response);
    }

    // Il doPost gestisce tutte le azioni AJAX
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            switch (action) {
                // Azioni per i PRODOTTI
                case "getProducts":
                    try {
                        List<ProductDTO> products = productDAO.findAll("ID"); // Usa l'ordine che preferisci
                        String json = gson.toJson(products);
                        response.getWriter().write(json);
                    } catch (SQLException e) {
                        log("Errore nel recupero dei prodotti", e);
                        sendJsonResponse(response, false, "Errore nel caricamento dei prodotti.", 500);
                    }
                    break;
                case "getProductDetails":
                    try {
                        int productId = Integer.parseInt(request.getParameter("productId"));
                        ProductDTO product = productDAO.findById(productId);
                        response.getWriter().write(gson.toJson(product));
                    } catch (Exception e) {
                        sendJsonResponse(response, false, "Prodotto non trovato.", 404);
                    }
                    break;

                case "saveProduct":
                    try {
                        String productIdStr = request.getParameter("productId");
                        ProductDTO product = new ProductDTO();
                        product.setName(request.getParameter("name"));
                        product.setBrand(request.getParameter("brand"));
                        product.setPrice(Float.parseFloat(request.getParameter("price")));
                        product.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
                        product.setCategory(ProductDTO.Category.valueOf(request.getParameter("category")));
                        product.setDescription(request.getParameter("description"));

                        // --- RIGHE AGGIUNTE ---
                        product.setGrade(ProductDTO.Grade.valueOf(request.getParameter("grade")));
                        product.setVat(Float.parseFloat(request.getParameter("vat")));

                        if (productIdStr == null || productIdStr.isEmpty()) {
                            // CREA (ID è vuoto)
                            productDAO.save(product);
                        } else {
                            // AGGIORNA
                            product.setId(Integer.parseInt(productIdStr));
                            productDAO.update(product);
                        }
                        sendJsonResponse(response, true, "Prodotto salvato con successo.", 200);
                    } catch (SQLException e) {
                        sendJsonResponse(response, false, "Errore database: " + e.getMessage(), 500);
                    } catch (IllegalArgumentException e) { // Aggiunto per gestire errori di conversione (es. enum non valido)
                        sendJsonResponse(response, false, "Dati non validi: " + e.getMessage(), 400);
                    }
                    break;

                case "deleteProduct":
                    try {
                        int productId = Integer.parseInt(request.getParameter("productId"));
                        productDAO.delete(productId);
                        sendJsonResponse(response, true, "Prodotto eliminato.", 200);
                    } catch (Exception e) {
                        sendJsonResponse(response, false, "Errore durante l'eliminazione.", 500);
                    }
                    break;

                // Azioni per gli ORDINI
                case "getOrders":
                    // Logica per recuperare gli ordini con i filtri applicati
                    handleGetOrders(request, response);
                    break;

                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Azione non riconosciuta.")));
                    break;
            }
        } catch (SQLException e) {
            log("Errore SQL nell'AdminServlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Errore del server.")));
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(gson.toJson(Map.of("success", success, "message", message)));
    }

    private void handleGetOrders(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String userIdStr = request.getParameter("userId");

        //TODO
        // Qui chiami il tuo OrderDAO con un metodo flessibile
        // Esempio: List<OrderDTO> orders = orderDAO.findWithFilters(startDate, endDate, userId);
        // Converti la lista in JSON e la invii
        // response.getWriter().write(gson.toJson(orders));
    }

    // ... implementa gli altri metodi handle... per i prodotti
}