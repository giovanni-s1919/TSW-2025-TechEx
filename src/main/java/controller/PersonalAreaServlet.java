package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.view.OrderConfirmationItem;
import util.HeaderDataHelper;
import util.Utility;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dto.UserDTO;
import model.dao.UserDAO;
import model.dto.AddressDTO;
import model.dao.AddressDAO;
import model.dto.UserAddressDTO;
import model.dao.UserAddressDAO;
import model.dto.PaymentMethodDTO;
import model.dao.PaymentMethodDAO;
import model.dto.ProductDTO;
import model.dao.ProductDAO;
import model.dao.OrderDAO;
import model.dto.OrderDTO;
import model.dto.OrderItemDTO;
import model.dao.OrderItemDAO;
import model.dto.OrderAddressDTO;
import model.dao.OrderAddressDAO;


@WebServlet(name = "PersonalAreaServlet", value = {"/personal_area"})
public class PersonalAreaServlet extends HttpServlet {
    private UserDAO userDAO;
    private AddressDAO addressDAO;
    private UserAddressDAO userAddressDAO;
    private PaymentMethodDAO paymentMethodDAO;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private OrderAddressDAO orderAddressDAO;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("datasource");
            if (dataSource == null) {
                throw new ServletException("DataSource non disponibile nel contesto della servlet.");
            }
            this.userDAO = new UserDAO(dataSource);
            this.addressDAO = new AddressDAO(dataSource);
            this.userAddressDAO = new UserAddressDAO(dataSource);
            this.paymentMethodDAO = new PaymentMethodDAO(dataSource);
            this.productDAO = new ProductDAO(dataSource);
            this.orderDAO = new OrderDAO(dataSource);
            this.orderItemDAO = new OrderItemDAO(dataSource);
            this.orderAddressDAO = new OrderAddressDAO(dataSource);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>() {
                @Override
                public JsonElement serialize(Timestamp src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                    return src == null ? null : new JsonPrimitive(src.toInstant().toString());
                }
            });
            gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                    return src == null ? null : new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
                }
            });
            this.gson = gsonBuilder.create();
        } catch (ServletException e) {
            log("Errore durante l'inizializzazione dei DAO", e);
            throw e;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (UserDTO) session.getAttribute("user");
        String action = request.getParameter("action");
        if ("getAddresses".equals(action)) {
            handleGetAddresses(request, response);
            return;
        }
        else if ("getAddressDetails".equals(action)) {
            handleGetAddressDetails(request, response);
            return;
        }
        else if ("getPaymentMethods".equals(action)) {
            handleGetPaymentMethods(request, response);
            return;
        }
        else if ("getPaymentMethodDetails".equals(action)) {
            handleGetPaymentMethodDetails(request, response);
            return;
        }
        else if ("getOrders".equals(action)) {
            handleGetOrders(request, response);
            return;
        }
        else if ("getOrderDetails".equals(action)) {
            handleGetOrderDetails(request, response);
            return;
        }
        if (session == null || loggedInUser == null || loggedInUser.getId() == 0) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            HeaderDataHelper.loadHeaderData(request, productDAO);
            UserDTO userFromDb = userDAO.findById(loggedInUser.getId());
            if (userFromDb != null) {
                request.setAttribute("userProfile", userFromDb);
            } else {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login?error=userNotFound");
                return;
            }
        } catch (SQLException e) {
            log("Errore nel recupero dell'utente dal database", e);
            request.setAttribute("errorMessage", "Si è verificato un errore nel caricamento del tuo profilo.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/personal_area.jsp")
                .forward(request, response);
    }

    private void handleGetAddresses(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        if (loggedInUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Utente non autenticato.")));
            return;
        }
        try {
            List<UserAddressDTO> userAddressLinks = userAddressDAO.findByUserID(loggedInUser.getId());
            List<Map<String, Object>> addressListForJson = new ArrayList<>();
            for (UserAddressDTO link : userAddressLinks) {
                AddressDTO addressDetails = addressDAO.findById(link.getAddressId());
                if (addressDetails != null) {
                    Map<String, Object> addressMap = new HashMap<>();
                    addressMap.put("id", addressDetails.getId());
                    addressMap.put("name", addressDetails.getName());
                    addressMap.put("surname", addressDetails.getSurname());
                    addressMap.put("street", addressDetails.getStreet());
                    addressMap.put("additionalInfo", addressDetails.getAdditionalInfo());
                    addressMap.put("city", addressDetails.getCity());
                    addressMap.put("postalCode", addressDetails.getPostalCode());
                    addressMap.put("region", addressDetails.getRegion());
                    addressMap.put("country", addressDetails.getCountry());
                    addressMap.put("phone", addressDetails.getPhone());
                    addressMap.put("isDefault", link.isDefault());
                    addressMap.put("addressType", addressDetails.getAddressType().name());
                    addressMap.put("translatedAddressType", addressDetails.getAddressType().toString());
                    addressListForJson.add(addressMap);
                }
            }
            response.getWriter().write(gson.toJson(addressListForJson));
        } catch (SQLException e) {
            log("Errore nel recupero degli indirizzi per l'utente " + loggedInUser.getId(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Errore del server durante il recupero degli indirizzi.")));
        }
    }

    private void handleGetAddressDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        if (loggedInUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Utente non autenticato.")));
            return;
        }
        try {
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            UserAddressDTO link = userAddressDAO.findById(addressId, loggedInUser.getId());
            if (link == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Accesso non autorizzato.")));
                return;
            }
            AddressDTO address = addressDAO.findById(addressId);
            if (address != null) {
                address.setDefault(link.isDefault());
                response.getWriter().write(gson.toJson(address));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Indirizzo non trovato.")));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Richiesta non valida o errore del server.")));
            log("Errore durante il recupero dei dettagli dell'indirizzo", e);
        }
    }

    private void handleGetPaymentMethods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        if (loggedInUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Utente non autenticato.")));
            return;
        }
        try {
            List<PaymentMethodDTO> paymentMethods = paymentMethodDAO.findByUserID(loggedInUser.getId());
            List<Map<String, Object>> paymentMethodsForJson = new ArrayList<>();
            for (PaymentMethodDTO pm : paymentMethods) {
                Map<String, Object> pmMap = new HashMap<>();
                pmMap.put("id", pm.getId());
                pmMap.put("name", pm.getName());
                pmMap.put("expiration", pm.getExpiration().toString());
                pmMap.put("isDefault", pm.isDefault());
                pmMap.put("maskedNumber", pm.getMaskedNumber());
                pmMap.put("cardType", pm.getCardType());
                paymentMethodsForJson.add(pmMap);
            }
            response.getWriter().write(gson.toJson(paymentMethodsForJson));
        } catch (SQLException e) {
            log("Errore nel recupero dei metodi di pagamento per l'utente " + loggedInUser.getId(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("success", false, "message", "Errore del server durante il recupero dei dati.")));
        }
    }

    private void handleGetPaymentMethodDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        if (loggedInUser == null) {
            sendJsonResponse(response, false, "Utente non autenticato.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            int methodId = Integer.parseInt(request.getParameter("methodId"));
            PaymentMethodDTO paymentMethod = paymentMethodDAO.findById(methodId);
            if (paymentMethod == null || paymentMethod.getUserID() != loggedInUser.getId()) {
                sendJsonResponse(response, false, "Accesso non autorizzato.", HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            Map<String, Object> pmMap = new HashMap<>();
            pmMap.put("id", paymentMethod.getId());
            pmMap.put("name", paymentMethod.getName());
            pmMap.put("number", paymentMethod.getNumber()); // <-- Il campo cruciale
            pmMap.put("expiration", paymentMethod.getExpiration().toString());
            pmMap.put("isDefault", paymentMethod.isDefault());
            response.getWriter().write(gson.toJson(pmMap));
        } catch (Exception e) {
            sendJsonResponse(response, false, "Richiesta non valida o errore del server.", HttpServletResponse.SC_BAD_REQUEST);
            log("Errore durante il recupero dei dettagli del metodo di pagamento", e);
        }
    }

    private void handleGetOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        if (loggedInUser == null) {
            sendJsonResponse(response, false, "Utente non autenticato.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            List<OrderDTO> orders = orderDAO.findByUserId(loggedInUser.getId());
            List<Map<String, Object>> ordersForJson = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (OrderDTO order : orders) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getId());
                orderMap.put("orderDate", order.getOrderDate().toLocalDateTime().format(formatter));
                orderMap.put("orderStatus", order.getOrderStatus().toString());
                orderMap.put("totalAmount", order.getTotalAmount());
                ordersForJson.add(orderMap);
            }
            response.getWriter().write(gson.toJson(ordersForJson));
        } catch (SQLException e) {
            log("Errore nel recupero degli ordini per l'utente " + loggedInUser.getId(), e);
            sendJsonResponse(response, false, "Errore del server durante il recupero degli ordini.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetOrderDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (loggedInUser == null) {
            sendJsonResponse(response, false, "Utente non autenticato.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            OrderDTO order = orderDAO.findById(orderId);

            if (order == null || order.getUserID() != loggedInUser.getId()) {
                sendJsonResponse(response, false, "Ordine non trovato o accesso non autorizzato.", HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Carica i dati grezzi dal database
            List<OrderItemDTO> orderItems = orderItemDAO.findByOrderId(orderId);
            OrderAddressDTO shippingAddress = orderAddressDAO.findById(order.getShippingAddressId());
            OrderAddressDTO billingAddress = orderAddressDAO.findById(order.getBillingAddressId());

            // --- INIZIO LOGICA DI ARRICCHIMENTO DATI (LA PARTE CRUCIALE) ---
            // Creiamo una lista di oggetti "arricchiti" per gli articoli
            List<OrderConfirmationItem> displayItems = new ArrayList<>();
            for (OrderItemDTO item : orderItems) {
                // Per ogni articolo, cerchiamo il prodotto corrispondente per ottenere l'ID per l'immagine
                ProductDTO product = productDAO.findByName(item.getItemName());
                displayItems.add(new OrderConfirmationItem(item, product));
            }
            // --- FINE LOGICA DI ARRICCHIMENTO DATI ---

            // Raggruppa tutti i dati in una mappa per una risposta JSON completa
            Map<String, Object> orderDetails = new HashMap<>();
            orderDetails.put("order", order);
            // Passa la lista "arricchita", non quella grezza
            orderDetails.put("items", displayItems);
            orderDetails.put("shippingAddress", shippingAddress);
            orderDetails.put("billingAddress", billingAddress);

            response.getWriter().write(gson.toJson(orderDetails));

        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "ID ordine non valido.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            log("Errore SQL durante il recupero dei dettagli dell'ordine.", e);
            sendJsonResponse(response, false, "Errore del server.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (UserDTO) session.getAttribute("user");
        if (session == null || loggedInUser == null || loggedInUser.getId() == 0) {
            sendJsonResponse(response, false, "Sessione scaduta o utente non autenticato. Riloggarsi.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        UserDTO currentUser = null;
        try {
            currentUser = userDAO.findById(loggedInUser.getId());
            if (currentUser == null) {
                sendJsonResponse(response, false, "Utente non trovato nel database.", HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } catch (SQLException e) {
            log("Errore nel recupero dell'utente per l'aggiornamento.", e);
            sendJsonResponse(response, false, "Errore interno del server durante la verifica utente.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        String action = request.getParameter("action");
        if ("updateField".equals(action)) {
            String field = request.getParameter("field");
            String value = request.getParameter("value");
            if (field == null || value == null || field.trim().isEmpty()) {
                sendJsonResponse(response, false, "Dati di aggiornamento incompleti.", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                boolean fieldUpdated = false;
                String successMessage = "";
                switch (field) {
                    case "name":
                        currentUser.setName(value);
                        successMessage = "Nome aggiornato con successo.";
                        fieldUpdated = true;
                        break;
                    case "surname":
                        currentUser.setSurname(value);
                        successMessage = "Cognome aggiornato con successo.";
                        fieldUpdated = true;
                        break;
                    case "username":
                        UserDTO existingUserByUsername = userDAO.findByUsername(value);
                        if (existingUserByUsername != null && existingUserByUsername.getId() != currentUser.getId()) {
                            sendJsonResponse(response, false, "Username già in uso.", HttpServletResponse.SC_CONFLICT);
                            return;
                        }
                        currentUser.setUsername(value);
                        successMessage = "Username aggiornato con successo.";
                        fieldUpdated = true;
                        break;
                    case "email":
                        String emailPattern = "\\A[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\\z";
                        if (value == null || !value.matches(emailPattern)) {
                            sendJsonResponse(response, false, "Formato email non valido.", HttpServletResponse.SC_BAD_REQUEST);
                            return;
                        }
                        UserDTO existingUserByEmail = userDAO.findByEmail(value);
                        if (existingUserByEmail != null && existingUserByEmail.getId() != currentUser.getId()) {
                            sendJsonResponse(response, false, "Email già in uso.", HttpServletResponse.SC_CONFLICT);
                            return;
                        }
                        currentUser.setEmail(value);
                        successMessage = "Email aggiornata con successo.";
                        fieldUpdated = true;
                        break;
                    case "birthDate":
                        try {
                            LocalDate birthDate = LocalDate.parse(value);
                            LocalDate today = LocalDate.now();
                            LocalDate eighteenYearsAgo = today.minusYears(18);
                            boolean isValid = true;
                            String errorMessage = "";
                            if (birthDate.isAfter(today)) {
                                isValid = false;
                                errorMessage = "La data di nascita non può essere una data futura.";
                            }
                            if (isValid && birthDate.isAfter(eighteenYearsAgo)) {
                                isValid = false;
                                errorMessage = "Devi avere almeno 18 anni!";
                            }
                            if (isValid) {
                                currentUser.setBirthDate(birthDate);
                                successMessage = "Data di nascita aggiornata con successo.";
                                fieldUpdated = true;
                            } else {
                                sendJsonResponse(response, false, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
                            }
                        } catch (java.time.format.DateTimeParseException e) {
                            sendJsonResponse(response, false, "Formato data non valido (YYYY-MM-DD).", HttpServletResponse.SC_BAD_REQUEST);
                        }
                        break;
                    case "phone":
                        currentUser.setPhone(value);
                        successMessage = "Telefono aggiornato con successo.";
                        fieldUpdated = true;
                        break;
                    default:
                        sendJsonResponse(response, false, "Campo non valido o non modificabile.", HttpServletResponse.SC_BAD_REQUEST);
                        return;
                }
                if (fieldUpdated) {
                    userDAO.update(currentUser);
                    session.setAttribute("user", currentUser);
                    sendJsonResponse(response, true, successMessage, HttpServletResponse.SC_OK);
                } else {
                    sendJsonResponse(response, false, "Nessuna modifica da salvare.", HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (SQLException e) {
                log("Errore durante l'aggiornamento del campo " + field + " per l'utente " + currentUser.getId(), e);
                sendJsonResponse(response, false, "Errore del database durante l'aggiornamento.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IllegalArgumentException e) {
                log("Errore di validazione durante l'aggiornamento: " + e.getMessage(), e);
                sendJsonResponse(response, false, "Errore di validazione: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if ("changePassword".equals(action)) {
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");
            if (currentPassword == null || newPassword == null || confirmNewPassword == null ||
                    currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmNewPassword.trim().isEmpty()) {
                sendJsonResponse(response, false, "Tutti i campi della password sono obbligatori.", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (!newPassword.equals(confirmNewPassword)) {
                sendJsonResponse(response, false, "La nuova password e la conferma non corrispondono.", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            boolean isLengthValid = newPassword.length() >= 8;
            boolean hasEnoughUppercases = newPassword.matches(".*[A-Z].*[A-Z].*");
            boolean hasEnoughLowercases = newPassword.matches(".*[a-z].*[a-z].*");
            boolean hasSpecialChar = newPassword.matches(".*[^a-zA-Z0-9].*");
            boolean hasNumber = newPassword.matches(".*[0-9].*");
            boolean isPasswordValid = isLengthValid && hasEnoughUppercases && hasEnoughLowercases && hasSpecialChar && hasNumber;
            if (!isPasswordValid) {
                String policyMessage = "La password non è valida. Deve rispettare i seguenti criteri: lunga almeno 8 caratteri, contenere almeno 2 lettere maiuscole, contenere almeno 2 lettere minuscole, contenere almeno 1 carattere speciale e almeno 1 numero.";
                sendJsonResponse(response, false, policyMessage, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                if (!Utility.checkPassword(currentPassword, currentUser.getPasswordHash())) {
                    sendJsonResponse(response, false, "La password attuale non è corretta.", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                String newPasswordHash = Utility.hashPassword(newPassword);
                currentUser.setPasswordHash(newPasswordHash);
                userDAO.update(currentUser);
                sendJsonResponse(response, true, "Password aggiornata con successo.", HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                log("Errore del database durante il cambio password per l'utente " + currentUser.getId(), e);
                sendJsonResponse(response, false, "Errore del database durante il cambio password.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                log("Errore durante l'hashing della nuova password.", e);
                sendJsonResponse(response, false, "Errore interno durante il cambio password.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if ("addAddress".equals(action)) {
            try {
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String street = request.getParameter("street");
                String additionalInfo = request.getParameter("additionalInfo");
                String city = request.getParameter("city");
                String postalCode = request.getParameter("postalCode");
                String region = request.getParameter("region");
                String country = request.getParameter("country");
                String phone = request.getParameter("phone");
                String addressTypeStr = request.getParameter("addressType");
                boolean isDefault = Boolean.parseBoolean(request.getParameter("isDefault"));
                AddressDTO newAddress = new AddressDTO();
                newAddress.setName(name);
                newAddress.setSurname(surname);
                newAddress.setStreet(street);
                newAddress.setAdditionalInfo(additionalInfo);
                newAddress.setCity(city);
                newAddress.setPostalCode(postalCode);
                newAddress.setRegion(region);
                newAddress.setCountry(country);
                newAddress.setPhone(phone);
                newAddress.setAddressType(AddressDTO.AddressType.valueOf(addressTypeStr));
                addressDAO.save(newAddress);
                if (isDefault) {
                    List<UserAddressDTO> existingAddresses = userAddressDAO.findByUserID(loggedInUser.getId());
                    for (UserAddressDTO addrLink : existingAddresses) {
                        if (addrLink.isDefault()) {
                            addrLink.setDefault(false);
                            userAddressDAO.update(addrLink);
                        }
                    }
                }
                UserAddressDTO newUserAddressLink = new UserAddressDTO(newAddress.getId(), loggedInUser.getId(), isDefault);
                userAddressDAO.save(newUserAddressLink);
                sendJsonResponse(response, true, "Indirizzo aggiunto con successo!", HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                log("Errore SQL durante l'aggiunta dell'indirizzo per l'utente " + loggedInUser.getId(), e);
                sendJsonResponse(response, false, "Errore durante il salvataggio nel database.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IllegalArgumentException e) {
                log("Errore nei dati per l'aggiunta dell'indirizzo: " + e.getMessage());
                sendJsonResponse(response, false, "I dati inseriti non sono validi. Riprova.", HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if ("updateAddress".equals(action)) {
            try {
                int addressId = Integer.parseInt(request.getParameter("addressId"));
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String street = request.getParameter("street");
                String additionalInfo = request.getParameter("additionalInfo");
                String city = request.getParameter("city");
                String postalCode = request.getParameter("postalCode");
                String region = request.getParameter("region");
                String country = request.getParameter("country");
                String phone = request.getParameter("phone");
                String addressTypeStr = request.getParameter("addressType");
                boolean isDefault = Boolean.parseBoolean(request.getParameter("isDefault"));
                UserAddressDTO linkToUpdate = userAddressDAO.findById(addressId, loggedInUser.getId());
                if (linkToUpdate == null) {
                    sendJsonResponse(response, false, "Tentativo di modificare un indirizzo non autorizzato.", HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                AddressDTO updatedAddress = new AddressDTO();
                updatedAddress.setId(addressId);
                updatedAddress.setName(name);
                updatedAddress.setSurname(surname);
                updatedAddress.setStreet(street);
                updatedAddress.setAdditionalInfo(additionalInfo);
                updatedAddress.setCity(city);
                updatedAddress.setPostalCode(postalCode);
                updatedAddress.setRegion(region);
                updatedAddress.setCountry(country);
                updatedAddress.setPhone(phone);
                updatedAddress.setAddressType(AddressDTO.AddressType.valueOf(addressTypeStr));
                addressDAO.update(updatedAddress);
                if (isDefault) {
                    List<UserAddressDTO> existingAddresses = userAddressDAO.findByUserID(loggedInUser.getId());
                    for (UserAddressDTO addrLink : existingAddresses) {
                        if (addrLink.getAddressId() != addressId && addrLink.isDefault()) {
                            addrLink.setDefault(false);
                            userAddressDAO.update(addrLink);
                        }
                    }
                }
                linkToUpdate.setDefault(isDefault);
                userAddressDAO.update(linkToUpdate);
                sendJsonResponse(response, true, "Indirizzo aggiornato con successo.", HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                log("Errore SQL durante l'aggiornamento dell'indirizzo: " + e.getMessage());
                sendJsonResponse(response, false, "Errore durante l'aggiornamento nel database.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                log("Errore generico durante l'aggiornamento dell'indirizzo: " + e.getMessage());
                sendJsonResponse(response, false, "Dati non validi. Riprova.", HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if ("deleteAddress".equals(action)) {
            try {
                String addressIdStr = request.getParameter("addressId");
                if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
                    sendJsonResponse(response, false, "ID indirizzo mancante.", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                int addressId = Integer.parseInt(addressIdStr);
                UserAddressDTO link = userAddressDAO.findById(addressId, loggedInUser.getId());
                if (link == null) {
                    sendJsonResponse(response, false, "Tentativo di eliminare un indirizzo non autorizzato.", HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                boolean wasDefault = link.isDefault();
                userAddressDAO.delete(addressId, loggedInUser.getId());
                addressDAO.delete(addressId);
                if (wasDefault) {
                    List<UserAddressDTO> remainingAddresses = userAddressDAO.findByUserID(loggedInUser.getId());
                    if (!remainingAddresses.isEmpty()) {
                        UserAddressDTO newDefault = remainingAddresses.get(0);
                        newDefault.setDefault(true);
                        userAddressDAO.update(newDefault);
                    }
                }
                sendJsonResponse(response, true, "Indirizzo eliminato con successo.", HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                sendJsonResponse(response, false, "ID indirizzo non valido.", HttpServletResponse.SC_BAD_REQUEST);
            } catch (SQLException e) {
                log("Errore SQL durante l'eliminazione dell'indirizzo: " + e.getMessage());
                sendJsonResponse(response, false, "Errore del database durante l'eliminazione.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if ("addPaymentMethod".equals(action)) {
            try {
                String name = request.getParameter("name");
                String rawNumber = request.getParameter("number");
                String cleanedNumber = rawNumber.replaceAll("[^0-9]", "");
                String expirationStr = request.getParameter("expiration");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
                YearMonth yearMonth = YearMonth.parse(expirationStr, formatter);
                LocalDate expiration = yearMonth.atEndOfMonth();
                boolean isDefault = Boolean.parseBoolean(request.getParameter("isDefault"));
                PaymentMethodDTO newPaymentMethod = new PaymentMethodDTO();
                newPaymentMethod.setUserID(loggedInUser.getId());
                newPaymentMethod.setName(name);
                newPaymentMethod.setNumber(cleanedNumber);
                newPaymentMethod.setExpiration(expiration);
                newPaymentMethod.setDefault(isDefault);
                if (isDefault) {
                    List<PaymentMethodDTO> existingMethods = paymentMethodDAO.findByUserID(loggedInUser.getId());
                    for (PaymentMethodDTO pm : existingMethods) {
                        if (pm.isDefault()) {
                            pm.setDefault(false);
                            paymentMethodDAO.update(pm);
                        }
                    }
                }
                paymentMethodDAO.save(newPaymentMethod);
                sendJsonResponse(response, true, "Metodo di pagamento aggiunto con successo!", HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                log("Errore SQL durante l'aggiunta del metodo di pagamento: " + e.getMessage());
                sendJsonResponse(response, false, "Errore durante il salvataggio nel database.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                log("Errore nei dati per l'aggiunta del metodo di pagamento: " + e.getMessage());
                sendJsonResponse(response, false, "I dati inseriti non sono validi. Riprova. (" + e.getMessage() + ")", HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if ("updatePaymentMethod".equals(action)) {
            try {
                int methodId = Integer.parseInt(request.getParameter("methodId"));
                String name = request.getParameter("name");
                String rawNumber = request.getParameter("number");
                String cleanedNumber = rawNumber.replaceAll("[^0-9]", "");
                String expirationStr = request.getParameter("expiration");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
                YearMonth yearMonth = YearMonth.parse(expirationStr, formatter);
                LocalDate expiration = yearMonth.atEndOfMonth();
                boolean isDefault = Boolean.parseBoolean(request.getParameter("isDefault"));
                PaymentMethodDTO methodToUpdate = paymentMethodDAO.findById(methodId);
                if (methodToUpdate == null || methodToUpdate.getUserID() != loggedInUser.getId()) {
                    sendJsonResponse(response, false, "Tentativo di modificare un metodo non autorizzato.", HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                methodToUpdate.setName(name);
                methodToUpdate.setNumber(cleanedNumber);
                methodToUpdate.setExpiration(expiration);
                methodToUpdate.setDefault(isDefault);
                if (isDefault) {
                    List<PaymentMethodDTO> existingMethods = paymentMethodDAO.findByUserID(loggedInUser.getId());
                    for (PaymentMethodDTO pm : existingMethods) {
                        if (pm.getId() != methodId && pm.isDefault()) {
                            pm.setDefault(false);
                            paymentMethodDAO.update(pm);
                        }
                    }
                }
                paymentMethodDAO.update(methodToUpdate);
                sendJsonResponse(response, true, "Metodo di pagamento aggiornato con successo.", HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                log("Errore SQL durante l'aggiornamento del metodo di pagamento: " + e.getMessage());
                sendJsonResponse(response, false, "Errore durante l'aggiornamento nel database.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                log("Errore nei dati per l'aggiornamento del metodo di pagamento: " + e.getMessage());
                sendJsonResponse(response, false, "I dati inseriti non sono validi. Riprova.", HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if ("deletePaymentMethod".equals(action)) {
            try {
                int methodId = Integer.parseInt(request.getParameter("methodId"));
                PaymentMethodDTO pmToDelete = paymentMethodDAO.findById(methodId);
                if (pmToDelete == null || pmToDelete.getUserID() != loggedInUser.getId()) {
                    sendJsonResponse(response, false, "Tentativo di eliminare un metodo di pagamento non autorizzato.", HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                boolean wasDefault = pmToDelete.isDefault();
                paymentMethodDAO.delete(methodId);
                if (wasDefault) {
                    List<PaymentMethodDTO> remainingMethods = paymentMethodDAO.findByUserID(loggedInUser.getId());
                    if (!remainingMethods.isEmpty()) {
                        PaymentMethodDTO newDefault = remainingMethods.get(0);
                        newDefault.setDefault(true);
                        paymentMethodDAO.update(newDefault);
                    }
                }
                sendJsonResponse(response, true, "Metodo di pagamento eliminato con successo.", HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                log("Errore SQL durante l'eliminazione del metodo di pagamento: " + e.getMessage());
                sendJsonResponse(response, false, "Errore del database durante l'eliminazione.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (NumberFormatException e) {
                sendJsonResponse(response, false, "ID metodo di pagamento non valido.", HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else {
            sendJsonResponse(response, false, "Azione non riconosciuta.", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("success", success);
        jsonResponse.put("message", message);
        response.getWriter().write(gson.toJson(jsonResponse));
    }
}