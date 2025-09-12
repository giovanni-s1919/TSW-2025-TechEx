package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import model.dto.UserDTO;
import model.dto.AddressDTO;
import model.dao.AddressDAO;
import model.dto.ProductDTO;
import model.dao.ProductDAO;
import model.dto.PaymentMethodDTO;
import model.dao.PaymentMethodDAO;
import model.dto.CartDTO;
import model.dao.CartDAO;
import model.dto.CartItemDTO;
import model.dao.CartItemDAO;
import model.view.CartDisplayItem;
import model.dto.OrderDTO;
import model.dao.OrderDAO;
import model.dto.OrderItemDTO;
import model.dao.OrderItemDAO;
import model.dto.OrderAddressDTO;
import model.dao.OrderAddressDAO;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private AddressDAO addressDAO;
    private PaymentMethodDAO paymentMethodDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private OrderAddressDAO orderAddressDAO;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto della servlet.");
        }
        this.productDAO = new ProductDAO(ds);
        this.cartDAO = new CartDAO(ds);
        this.cartItemDAO = new CartItemDAO(ds);
        this.addressDAO = new AddressDAO(ds);
        this.paymentMethodDAO = new PaymentMethodDAO(ds);
        this.orderDAO = new OrderDAO(ds);
        this.orderItemDAO = new OrderItemDAO(ds);
        this.orderAddressDAO = new OrderAddressDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String from = request.getParameter("from");
        if (from != null && session != null) {
            session.setAttribute("checkoutSource", from);
        }
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        String role = (user != null) ? user.getRole().name() : "Guest";
        request.setAttribute("role", role);
        List<CartDisplayItem> checkoutItems = new ArrayList<>();
        float checkoutTotal = 0.0f;
        try {
            if ("cart".equals(from) && user != null) {
                CartDTO cart = cartDAO.findByUserID(user.getId());
                if (cart != null) {
                    List<CartItemDTO> cartItems = cartItemDAO.findByCartID(cart.getId());
                    for (CartItemDTO item : cartItems) {
                        ProductDTO product = productDAO.findById(item.getProductID());
                        if (product != null) {
                            checkoutItems.add(new CartDisplayItem(product, item.getQuantity()));
                            checkoutTotal += product.getPrice() * item.getQuantity();
                        }
                    }
                }
            }
            else if ("buyNow".equals(from) && session != null) {
                Integer productId = (Integer) session.getAttribute("buyNowProduct");
                Integer quantity = Integer.parseInt((String) session.getAttribute("buyNowQuantity"));
                if (productId != null && quantity != null) {
                    ProductDTO product = productDAO.findById(productId);
                    if (product != null) {
                        checkoutItems.add(new CartDisplayItem(product, quantity));
                        checkoutTotal += product.getPrice() * quantity;
                    }
                }
            }
            if (checkoutItems.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            request.setAttribute("checkoutItems", checkoutItems);
            request.setAttribute("checkoutTotal", checkoutTotal);
            if (user != null) {
                List<AddressDTO> addresses = addressDAO.findAddressesByUserId(user.getId());
                List<PaymentMethodDTO> paymentMethods = paymentMethodDAO.findByUserID(user.getId());
                request.setAttribute("userAddresses", addresses);
                request.setAttribute("userPaymentMethods", paymentMethods);
            }
        } catch (SQLException e) {
            log("Errore DB durante la preparazione del checkout", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile preparare il checkout.");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new Gson().toJson(Map.of("success", false, "message", "Sessione non valida.")));
            return;
        }
        UserDTO user = (UserDTO) session.getAttribute("user");
        String action = request.getParameter("action");
        if ("placeOrder".equals(action)) {
            Connection conn = null;
            try {
                DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
                conn = ds.getConnection();
                conn.setAutoCommit(false);
                List<CartDisplayItem> itemsToPurchase = new ArrayList<>();
                float totalAmount = 0.0f;
                String from = (String) session.getAttribute("checkoutSource");
                if ("cart".equals(from) && user != null) {
                    CartDTO cart = cartDAO.findByUserID(user.getId());
                    if (cart != null) {
                        List<CartItemDTO> cartItems = cartItemDAO.findByCartID(cart.getId());
                        for (CartItemDTO item : cartItems) {
                            ProductDTO product = productDAO.findById(item.getProductID());
                            if (product != null && product.getStockQuantity() >= item.getQuantity()) {
                                itemsToPurchase.add(new CartDisplayItem(product, item.getQuantity()));
                                totalAmount += product.getPrice() * item.getQuantity();
                            } else {
                                throw new SQLException("Prodotto '" + product.getName() + "' non disponibile o quantità in stock insufficiente.");
                            }
                        }
                    }
                } else if ("buyNow".equals(from)) {
                    Integer productId = (Integer) session.getAttribute("buyNowProduct");
                    Integer quantity = Integer.parseInt((String) session.getAttribute("buyNowQuantity"));
                    ProductDTO product = productDAO.findById(productId);
                    if (product != null && product.getStockQuantity() >= quantity) {
                        itemsToPurchase.add(new CartDisplayItem(product, quantity));
                        totalAmount += product.getPrice() * quantity;
                    } else {
                        throw new SQLException("Prodotto non disponibile o quantità in stock insufficiente.");
                    }
                }
                OrderAddressDTO orderAddress = new OrderAddressDTO();
                orderAddress.setName(request.getParameter("name"));
                orderAddress.setSurname(request.getParameter("surname"));
                orderAddress.setStreet(request.getParameter("street"));
                orderAddress.setCity(request.getParameter("city"));
                orderAddress.setPostalCode(request.getParameter("postalCode"));
                orderAddress.setRegion(request.getParameter("region"));
                orderAddress.setCountry(request.getParameter("country"));
                orderAddress.setPhone(request.getParameter("phone"));
                orderAddress.setAddressType(OrderAddressDTO.AddressType.Shipping);
                orderAddressDAO.save(orderAddress, conn);
                OrderDTO newOrder = new OrderDTO();
                newOrder.setUserID(user != null ? user.getId() : 0);
                newOrder.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
                newOrder.setOrderStatus("Processing");
                newOrder.setTotalAmount(totalAmount);
                newOrder.setShippingAddressId(orderAddress.getId());
                newOrder.setBillingAddressId(orderAddress.getId());
                orderDAO.save(newOrder, conn);
                for (CartDisplayItem item : itemsToPurchase) {
                    ProductDTO product = item.getProduct();
                    OrderItemDTO orderItem = new OrderItemDTO();
                    orderItem.setOrderID(newOrder.getId());
                    orderItem.setItemName(product.getName());
                    orderItem.setItemDescription(product.getDescription());
                    orderItem.setItemBrand(product.getBrand());
                    orderItem.setItemPrice(product.getPrice());
                    orderItem.setItemCategory(OrderItemDTO.Category.valueOf(product.getCategory().name()));
                    orderItem.setItemGrade(OrderItemDTO.Grade.valueOf(product.getGrade().name()));
                    orderItem.setItemQuantity(item.getQuantity());
                    orderItem.setItemVAT(product.getVat());
                    orderItemDAO.save(orderItem, conn);
                    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                    productDAO.update(product, conn);
                }
                if ("cart".equals(from) && user != null) {
                    CartDTO cart = cartDAO.findByUserID(user.getId());
                    if (cart != null) {
                        cartItemDAO.deleteByCartId(cart.getId(), conn);
                    }
                }
                session.removeAttribute("buyNowProduct");
                session.removeAttribute("buyNowQuantity");
                session.removeAttribute("checkoutSource");
                conn.commit();
                response.getWriter().write(new Gson().toJson(Map.of("success", true, "orderId", newOrder.getId())));
            } catch (Exception e) {
                if (conn != null) try { conn.rollback(); } catch (SQLException ex) { log("Rollback fallito", ex); }
                log("Errore creazione ordine: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(new Gson().toJson(Map.of("success", false, "message", "Errore durante la creazione dell'ordine.")));
            } finally {
                if (conn != null) try { conn.close(); } catch (SQLException e) { log("Chiusura connessione fallita", e); }
            }
        }
    }
}