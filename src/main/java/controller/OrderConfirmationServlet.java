package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.OrderAddressDAO;
import model.dao.OrderDAO;
import model.dao.OrderItemDAO;
import model.dto.OrderAddressDTO;
import model.dto.OrderDTO;
import model.dto.OrderItemDTO;
import model.dto.UserDTO;
import model.dto.ProductDTO;
import model.dao.ProductDAO;
import model.view.OrderConfirmationItem;
import util.HeaderDataHelper;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderConfirmationServlet", value = "/order_confirmation")
public class OrderConfirmationServlet extends HttpServlet {
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private OrderAddressDAO orderAddressDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile.");
        }
        this.orderDAO = new OrderDAO(ds);
        this.orderItemDAO = new OrderItemDAO(ds);
        this.orderAddressDAO = new OrderAddressDAO(ds);
        this.productDAO = new ProductDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        String orderIdStr = request.getParameter("id");
        int orderId;
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        try {
            HeaderDataHelper.loadHeaderData(request, productDAO);
            String role = (user != null) ? user.getRole().name() : "Guest";
            request.setAttribute("role", role);
            orderId = Integer.parseInt(orderIdStr);
            OrderDTO order = orderDAO.findById(orderId);
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            boolean isOwner = (user != null && order.getUserID() == user.getId());
            boolean isAdmin = (user != null && user.getRole().name().equals("Admin"));
            boolean isGuestOrderOwner = (user == null && order.getUserID() == 999);

            if (!isOwner && !isAdmin && !isGuestOrderOwner) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            OrderAddressDTO shippingAddress = orderAddressDAO.findById(order.getShippingAddressId());
            OrderAddressDTO billingAddress = orderAddressDAO.findById(order.getBillingAddressId());
            List<OrderItemDTO> orderItems = orderItemDAO.findByOrderId(orderId);
            List<OrderConfirmationItem> displayItems = new ArrayList<>();
            float subtotal = 0;
            for (OrderItemDTO item : orderItems) {
                subtotal += item.getItemPrice() * item.getItemQuantity();
                ProductDTO product = productDAO.findByName(item.getItemName());
                displayItems.add(new OrderConfirmationItem(item, product));
            }
            float shippingCost = order.getTotalAmount() - subtotal;
            request.setAttribute("order", order);
            request.setAttribute("shippingAddress", shippingAddress);
            request.setAttribute("billingAddress", billingAddress);
            request.setAttribute("items", displayItems);
            request.setAttribute("subtotal", subtotal);
            request.setAttribute("shippingCost", shippingCost);
            request.getRequestDispatcher("/WEB-INF/jsp/order_confirmation.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (SQLException e) {
            log("Errore DB durante il recupero dei dettagli dell'ordine", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile recuperare i dettagli dell'ordine.");
        }
    }
}
