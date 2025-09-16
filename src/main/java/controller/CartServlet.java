package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CartDAO;
import model.dao.CartItemDAO;
import model.dao.ProductDAO;
import model.dto.CartDTO;
import model.dto.CartItemDTO;
import model.dto.ProductDTO;
import model.dto.UserDTO;
import model.view.CartDisplayItem;
import util.HeaderDataHelper;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", value = {"/cart"})
@MultipartConfig

public class CartServlet extends HttpServlet {
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;

    @Override
    public void init() {
        // CONFERMATO: uso "datasource" minuscolo come da tua indicazione.
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        cartDAO = new CartDAO(ds);
        cartItemDAO = new CartItemDAO(ds);
        productDAO = new ProductDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("role", user.getRole().toString());
        List<CartDisplayItem> cartDisplayItems = new ArrayList<>();
        Float cartTotal = 0.0f;

        try {
            HeaderDataHelper.loadHeaderData(request, productDAO);
            CartDTO cart = cartDAO.findByUserID(user.getId());
            if (cart != null) {
                List<CartItemDTO> cartItems = cartItemDAO.findByCartID(cart.getId());
                for (CartItemDTO cartItem : cartItems) {
                    ProductDTO product = productDAO.findById(cartItem.getProductID());
                    if (product != null) {
                        cartDisplayItems.add(new CartDisplayItem(product, cartItem.getQuantity()));
                        cartTotal += product.getPrice() * cartItem.getQuantity();
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Errore DB nel caricamento del carrello", e);
        }

        request.setAttribute("cartItems", cartDisplayItems);
        request.setAttribute("cartTotal", cartTotal);
        request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("updateQuantity".equals(action)) {
            handleAjaxUpdate(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void handleAjaxUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Utente non autenticato.\"}");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            CartDTO cart = cartDAO.findByUserID(user.getId());
            if (cart == null) throw new SQLException("Carrello utente non trovato.");

            CartItemDTO itemToUpdate = cartItemDAO.findByProductIDAndCartID(cart.getId(), productId);
            if (itemToUpdate == null) throw new SQLException("Articolo non trovato nel carrello.");

            if (quantity > 0) {
                itemToUpdate.setQuantity(quantity);
                cartItemDAO.update(itemToUpdate);
            } else {
                cartItemDAO.delete(itemToUpdate.getId());
            }

            List<CartItemDTO> allItems = cartItemDAO.findByCartID(cart.getId());
            float newCartTotal = 0.0f;
            for (CartItemDTO item : allItems) {
                ProductDTO p = productDAO.findById(item.getProductID());
                if (p != null) newCartTotal += p.getPrice() * item.getQuantity();
            }

            ProductDTO product = productDAO.findById(productId);
            float newSubtotal = product != null ? product.getPrice() * quantity : 0.0f;

            String newSubtotalFormatted = String.format("€%.2f", newSubtotal).replace(",", ".");
            String newCartTotalFormatted = String.format("€%.2f", newCartTotal).replace(",", ".");

            String jsonResponse = String.format(
                    "{\"success\": true, \"newSubtotalFormatted\": \"%s\", \"newCartTotalFormatted\": \"%s\", \"cartIsEmpty\": %b}",
                    newSubtotalFormatted, newCartTotalFormatted, allItems.isEmpty()
            );

            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            System.err.println("--- ERRORE AJAX IN CartServlet ---");
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Errore interno del server. Controlla i log per dettagli.\"}");
        }
    }
}