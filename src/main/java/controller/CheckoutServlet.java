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
import util.HeaderDataHelper;

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
        String fromParam = request.getParameter("from");
        if (fromParam != null) {
            session.setAttribute("checkoutSource", fromParam);
        }
        String from = (String) session.getAttribute("checkoutSource");
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        String role = (user != null) ? user.getRole().name() : "Guest";
        request.setAttribute("role", role);
        List<CartDisplayItem> checkoutItems = new ArrayList<>();
        float checkoutTotal = 0.0f;
        try {
            HeaderDataHelper.loadHeaderData(request, productDAO);
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
                List<AddressDTO> allAddresses = addressDAO.findAddressesByUserId(user.getId());
                List<PaymentMethodDTO> paymentMethods = paymentMethodDAO.findByUserID(user.getId());
                List<AddressDTO> shippingAddresses = new ArrayList<>();
                List<AddressDTO> billingAddresses = new ArrayList<>();
                for (AddressDTO addr : allAddresses) {
                    if (addr.getAddressType() == AddressDTO.AddressType.Shipping) {
                        shippingAddresses.add(addr);
                    }
                    if (addr.getAddressType() == AddressDTO.AddressType.Billing) {
                        billingAddresses.add(addr);
                    }
                }
                request.setAttribute("userShippingAddresses", shippingAddresses);
                request.setAttribute("userBillingAddresses", billingAddresses);
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
                if (user == null) {
                    String cardName = request.getParameter("cardName");
                    String cardNumber = request.getParameter("cardNumber");
                    String cardExpiration = request.getParameter("cardExpiration");
                    String cardCvc = request.getParameter("cardCvc");
                    if (cardName == null || cardName.trim().isEmpty() ||
                            cardNumber == null || cardNumber.trim().isEmpty() ||
                            cardExpiration == null || cardExpiration.trim().isEmpty() ||
                            cardCvc == null || cardCvc.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write(new Gson().toJson(Map.of("success", false, "message", "Tutti i campi del metodo di pagamento sono obbligatori.")));
                        return;
                    }
                } else {
                    String paymentMethodId = request.getParameter("paymentMethodId");
                    if (paymentMethodId == null || paymentMethodId.trim().isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write(new Gson().toJson(Map.of("success", false, "message", "Nessun metodo di pagamento selezionato.")));
                        return;
                    }
                }
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
                                throw new SQLException("Prodotto non disponibile o quantità in stock insufficiente.");
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
                final float SHIPPING_COST = 7.99f;
                float finalTotal = totalAmount;
                if (user != null && totalAmount > 50.0f) {
                } else {
                    finalTotal += SHIPPING_COST;
                }
                OrderAddressDTO shippingOrderAddress = new OrderAddressDTO();
                if (user != null) {
                    String addressIdStr = request.getParameter("addressId");
                    if (addressIdStr == null || addressIdStr.isEmpty()) {
                        throw new ServletException("ID indirizzo di spedizione non fornito per l'utente registrato.");
                    }
                    int addressId = Integer.parseInt(addressIdStr);
                    AddressDTO chosenAddress = addressDAO.findById(addressId);
                    if (chosenAddress == null) {
                        throw new ServletException("Indirizzo di spedizione selezionato con ID " + addressId + " non trovato.");
                    }
                    shippingOrderAddress.setName(chosenAddress.getName());
                    shippingOrderAddress.setSurname(chosenAddress.getSurname());
                    shippingOrderAddress.setStreet(chosenAddress.getStreet());
                    shippingOrderAddress.setCity(chosenAddress.getCity());
                    shippingOrderAddress.setPostalCode(chosenAddress.getPostalCode());
                    shippingOrderAddress.setRegion(chosenAddress.getRegion());
                    shippingOrderAddress.setCountry(chosenAddress.getCountry());
                    shippingOrderAddress.setPhone(chosenAddress.getPhone());
                } else {
                    shippingOrderAddress.setName(request.getParameter("name"));
                    shippingOrderAddress.setSurname(request.getParameter("surname"));
                    shippingOrderAddress.setStreet(request.getParameter("street"));
                    shippingOrderAddress.setCity(request.getParameter("city"));
                    shippingOrderAddress.setPostalCode(request.getParameter("postalCode"));
                    shippingOrderAddress.setRegion(request.getParameter("region"));
                    shippingOrderAddress.setCountry(request.getParameter("country"));
                    shippingOrderAddress.setPhone(request.getParameter("phone"));
                }
                shippingOrderAddress.setAddressType(OrderAddressDTO.AddressType.Shipping);
                orderAddressDAO.save(shippingOrderAddress, conn);
                OrderAddressDTO billingOrderAddress;
                String billingSameAsShipping = request.getParameter("billingSameAsShipping");
                if (billingSameAsShipping != null && billingSameAsShipping.equals("true")) {
                    billingOrderAddress = shippingOrderAddress;
                } else {
                    billingOrderAddress = new OrderAddressDTO();
                    if (user != null) {
                        String billingAddressIdStr = request.getParameter("billingAddressId");
                        if (billingAddressIdStr == null || billingAddressIdStr.isEmpty()) {
                            throw new ServletException("ID indirizzo di fatturazione non fornito.");
                        }
                        int billingAddressId = Integer.parseInt(billingAddressIdStr);
                        AddressDTO chosenBillingAddress = addressDAO.findById(billingAddressId);
                        if (chosenBillingAddress == null) {
                            throw new ServletException("Indirizzo di fatturazione selezionato con ID " + billingAddressId + " non trovato.");
                        }
                        billingOrderAddress.setName(chosenBillingAddress.getName());
                        billingOrderAddress.setSurname(chosenBillingAddress.getSurname());
                        billingOrderAddress.setStreet(chosenBillingAddress.getStreet());
                        billingOrderAddress.setCity(chosenBillingAddress.getCity());
                        billingOrderAddress.setPostalCode(chosenBillingAddress.getPostalCode());
                        billingOrderAddress.setRegion(chosenBillingAddress.getRegion());
                        billingOrderAddress.setCountry(chosenBillingAddress.getCountry());
                        billingOrderAddress.setPhone(chosenBillingAddress.getPhone());
                    } else {
                        billingOrderAddress.setName(request.getParameter("billing_name"));
                        billingOrderAddress.setSurname(request.getParameter("billing_surname"));
                        billingOrderAddress.setStreet(request.getParameter("billing_street"));
                        billingOrderAddress.setCity(request.getParameter("billing_city"));
                        billingOrderAddress.setPostalCode(request.getParameter("billing_postalCode"));
                        billingOrderAddress.setRegion(request.getParameter("billing_region"));
                        billingOrderAddress.setCountry(request.getParameter("billing_country"));
                        billingOrderAddress.setPhone(request.getParameter("billing_phone"));
                    }
                    billingOrderAddress.setAddressType(OrderAddressDTO.AddressType.Billing);
                    orderAddressDAO.save(billingOrderAddress, conn);
                }
                OrderDTO newOrder = new OrderDTO();
                if (user != null) {
                    newOrder.setUserID(user.getId());
                } else {
                    newOrder.setUserID(999);
                }
                newOrder.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
                newOrder.setOrderStatus(OrderDTO.OrderStatus.valueOf("Processing"));
                newOrder.setTotalAmount(finalTotal);
                newOrder.setShippingAddressId(shippingOrderAddress.getId());
                newOrder.setBillingAddressId(billingOrderAddress.getId());
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
                log("Errore creazione ordine: ", e);
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(new Gson().toJson(Map.of("success", false, "message", "Errore durante la creazione dell'ordine.")));
            } finally {
                if (conn != null) try { conn.close(); } catch (SQLException e) { log("Chiusura connessione fallita", e); }
            }
        }
    }
}