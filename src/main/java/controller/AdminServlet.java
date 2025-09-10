package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.OrderDAO;
import model.dao.ProductDAO;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
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
                    // Logica per recuperare tutti i prodotti e inviarli come JSON
                    break;
                case "getProductDetails":
                    // Logica per recuperare un singolo prodotto per la modifica
                    break;
                case "saveProduct":
                    // Logica per creare (se id è assente) o aggiornare (se id è presente) un prodotto
                    break;
                case "deleteProduct":
                    // Logica per cancellare un prodotto
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