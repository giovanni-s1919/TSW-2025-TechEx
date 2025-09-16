package controller;

// --- AGGIUNGI QUESTI IMPORT ---
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
// --- FINE AGGIUNGI IMPORT ---

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.dao.OrderDAO;
import model.dao.OrderItemDAO;
import model.dao.ProductDAO;
import model.dto.OrderDTO;
import model.dto.OrderItemDTO;
import model.dto.ProductDTO;
import util.HeaderDataHelper;


import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@WebServlet(name = "AdminServlet", value = {"/admin/panel"})
@MultipartConfig
public class AdminServlet extends HttpServlet {
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    // --- MODIFICA 1: Inizializzazione personalizzata di Gson ---
    private Gson gson;

    @Override
    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        this.productDAO = new ProductDAO(ds);
        this.orderDAO = new OrderDAO(ds);
        this.orderItemDAO = new OrderItemDAO(ds);

        // Crea un GsonBuilder per personalizzare la serializzazione
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Insegna a Gson come gestire LocalDate
        gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)); // Formato "YYYY-MM-DD"
            }
        });

        // Insegna a Gson come gestire Timestamp
        gsonBuilder.registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>() {
            @Override
            public JsonElement serialize(Timestamp src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toInstant().toString()); // Formato standard ISO-8601
            }
        });

        this.gson = gsonBuilder.create();
    }

    // --- Fine Modifica 1 ---

    // ... tutti gli altri metodi della servlet (doGet, doPost, etc.) rimangono ESATTAMENTE GLI STESSI ...
    // ... copia e incolla tutto il resto del tuo codice da qui in poi ...
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HeaderDataHelper.loadHeaderData(request, productDAO);
        } catch (SQLException e) {
            log("Errore durante il caricamento dei dati per l'header del pannello admin", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile caricare i dati della pagina.");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/admin_panel.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            sendJsonResponse(response, false, "Azione non specificata.", 400);
            return;
        }
        try {
            switch (action) {
                case "getProducts":
                    handleGetProducts(request, response);
                    break;
                case "getProductDetails":
                    handleGetProductDetails(request, response);
                    break;
                case "saveProduct":
                    handleSaveProduct(request, response);
                    break;
                case "deleteProduct":
                    handleDeleteProduct(request, response);
                    break;
                case "getOrders":
                    handleGetOrders(request, response);
                    break;
                case "getOrderDetails":
                    handleGetOrderDetails(request, response);
                    break;
                default:
                    sendJsonResponse(response, false, "Azione non riconosciuta.", 400);
                    break;
            }
        } catch (Exception e) {
            log("ERRORE FATALE in AdminServlet per azione '" + action + "':", e);
            sendJsonResponse(response, false, "Errore interno del server. Controllare i log.", 500);
        }
    }
    private void handleGetOrders(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        LocalDate startDate = null;
        LocalDate endDate = null;
        Integer customerId = null;
        try {
            String startDateStr = request.getParameter("startDate");
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            String endDateStr = request.getParameter("endDate");
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }
            String customerIdStr = request.getParameter("customerId");
            if (customerIdStr != null && !customerIdStr.trim().isEmpty()) {
                customerId = Integer.parseInt(customerIdStr);
            }
            List<OrderDTO> orders = orderDAO.findWithFilters(startDate, endDate, customerId);
            response.getWriter().write(gson.toJson(orders));
        } catch (DateTimeParseException e) {
            sendJsonResponse(response, false, "Formato data non valido. Usa YYYY-MM-DD.", 400);
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "L'ID cliente deve essere un numero.", 400);
        }
    }
    private void handleSaveProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        ProductDTO product = new ProductDTO();
        String productIdStr = request.getParameter("productId");
        product.setName(getPartValue(request.getPart("name")));
        product.setBrand(getPartValue(request.getPart("brand")));
        product.setPrice(Float.parseFloat(getPartValue(request.getPart("price"))));
        product.setStockQuantity(Integer.parseInt(getPartValue(request.getPart("stockQuantity"))));
        product.setCategory(ProductDTO.Category.valueOf(getPartValue(request.getPart("category"))));
        product.setGrade(ProductDTO.Grade.valueOf(getPartValue(request.getPart("grade"))));
        product.setVat(Float.parseFloat(getPartValue(request.getPart("vat"))));
        product.setDescription(getPartValue(request.getPart("description")));
        if (productIdStr == null || productIdStr.isEmpty()) {
            productDAO.save(product);
        } else {
            product.setId(Integer.parseInt(productIdStr));
            productDAO.update(product);
        }
        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            saveImageInBothLocations(filePart, product.getId());
        }
        sendJsonResponse(response, true, "Prodotto salvato con successo.", 200);
    }
    private void handleGetProducts(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        List<ProductDTO> products = productDAO.findAll("ID");
        response.getWriter().write(gson.toJson(products));
    }
    private void handleGetProductDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        ProductDTO product = productDAO.findById(productId);
        if (product != null) {
            response.getWriter().write(gson.toJson(product));
        } else {
            sendJsonResponse(response, false, "Prodotto non trovato.", 404);
        }
    }
    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        productDAO.delete(productId);
        sendJsonResponse(response, true, "Prodotto eliminato.", 200);
    }
    private String getPartValue(Part part) throws IOException {
        if (part == null) return null;
        try (InputStream inputStream = part.getInputStream();
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            return scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "";
        }
    }
    private void saveImageInBothLocations(Part filePart, int productId) throws IOException {
        String newFileName = productId + ".png";
        String deployedPathDir = getServletContext().getRealPath("/images/products");
        if (deployedPathDir == null) {
            throw new IOException("Impossibile trovare il percorso di deployment. L'applicazione potrebbe non essere 'esplosa'.");
        }
        File deployedUploadDir = new File(deployedPathDir);
        if (!deployedUploadDir.exists()) {
            deployedUploadDir.mkdirs();
        }
        cleanOldFiles(deployedUploadDir, productId);
        Path deployedFilePath = new File(deployedUploadDir, newFileName).toPath();
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, deployedFilePath, StandardCopyOption.REPLACE_EXISTING);
            log("Immagine salvata nel percorso di deployment: " + deployedFilePath);
        }
        File projectRoot = new File(getServletContext().getRealPath("")).getParentFile().getParentFile();
        String sourcePathDir = projectRoot.getAbsolutePath() + File.separator + "src" + File.separator + "main" +
                File.separator + "webapp" + File.separator + "images" + File.separator + "products";
        File sourceUploadDir = new File(sourcePathDir);
        if (!sourceUploadDir.exists()) {
            sourceUploadDir.mkdirs();
        }
        cleanOldFiles(sourceUploadDir, productId);
        Path sourceFilePath = new File(sourceUploadDir, newFileName).toPath();
        Files.copy(deployedFilePath, sourceFilePath, StandardCopyOption.REPLACE_EXISTING);
        log("Immagine copiata nel percorso del codice sorgente: " + sourceFilePath);
    }
    private void cleanOldFiles(File directory, int productId) {
        File[] oldFiles = directory.listFiles((dir, name) -> name.startsWith(productId + "."));
        if (oldFiles != null) {
            for (File file : oldFiles) {
                file.delete();
            }
        }
    }

    private void handleGetOrderDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            OrderDTO order = orderDAO.findById(orderId);
            List<OrderItemDTO> items = orderItemDAO.findByOrderId(orderId);

            if (order == null) {
                sendJsonResponse(response, false, "Ordine non trovato.", 404);
                return;
            }

            // Crea un oggetto Map per contenere sia l'ordine che i suoi articoli
            Map<String, Object> orderDetails = Map.of(
                    "order", order,
                    "items", items
            );

            response.getWriter().write(gson.toJson(orderDetails));

        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "ID ordine non valido.", 400);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(gson.toJson(Map.of("success", success, "message", message)));
    }
}