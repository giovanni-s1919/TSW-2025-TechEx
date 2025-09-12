package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.dao.OrderDAO;
import model.dao.ProductDAO;
import model.dto.ProductDTO;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@WebServlet(name = "AdminServlet", value = {"/admin/panel"})
@MultipartConfig
public class AdminServlet extends HttpServlet {
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private Gson gson = new Gson();

    @Override
    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        this.productDAO = new ProductDAO(ds);
        this.orderDAO = new OrderDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/admin_panel.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Legge l'azione dall'URL
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
                default:
                    sendJsonResponse(response, false, "Azione non riconosciuta.", 400);
                    break;
            }
        } catch (Exception e) {
            log("Errore nell'AdminServlet per azione " + action, e);
            sendJsonResponse(response, false, "Errore interno del server.", 500);
        }
    }

    // --- GESTORI DI AZIONI ---

    private void handleSaveProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        ProductDTO product = new ProductDTO();

        // Legge l'ID dall'URL (se presente)
        String productIdStr = request.getParameter("productId");

        // --- LETTURA CORRETTA DEI CAMPI DAL CORPO MULTIPART ---
        product.setName(getPartValue(request.getPart("name")));
        product.setBrand(getPartValue(request.getPart("brand")));
        product.setPrice(Float.parseFloat(getPartValue(request.getPart("price"))));
        product.setStockQuantity(Integer.parseInt(getPartValue(request.getPart("stockQuantity"))));
        product.setCategory(ProductDTO.Category.valueOf(getPartValue(request.getPart("category"))));
        product.setGrade(ProductDTO.Grade.valueOf(getPartValue(request.getPart("grade"))));
        product.setVat(Float.parseFloat(getPartValue(request.getPart("vat"))));
        product.setDescription(getPartValue(request.getPart("description")));

        // Salva o aggiorna il prodotto
        if (productIdStr == null || productIdStr.isEmpty()) {
            productDAO.save(product);
        } else {
            product.setId(Integer.parseInt(productIdStr));
            productDAO.update(product);
        }

        // Gestisce l'upload del file
        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            saveImageAsPng(filePart, product.getId());
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

    // --- FUNZIONI HELPER ---

    /**
     * Estrae il valore stringa da una Part di una richiesta multipart.
     */
    private String getPartValue(Part part) throws IOException {
        if (part == null) return null;
        try (InputStream inputStream = part.getInputStream();
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            return scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "";
        }
    }

    private void saveImageAsPng(Part filePart, int productId) throws IOException {
        String newFileName = productId + ".png";

        // --- 1. PERCORSO DI DEPLOYMENT (per la visibilità immediata) ---
        String deployedPathDir = getServletContext().getRealPath("/images/products");
        if (deployedPathDir == null) {
            throw new IOException("Impossibile trovare il percorso di deployment. L'applicazione potrebbe non essere 'esplosa'.");
        }
        File deployedUploadDir = new File(deployedPathDir);
        if (!deployedUploadDir.exists()) {
            deployedUploadDir.mkdirs();
        }

        // Pulisce i vecchi file in questa location
        cleanOldFiles(deployedUploadDir, productId);

        Path deployedFilePath = new File(deployedUploadDir, newFileName).toPath();

        // Salva il file nella cartella di deployment
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, deployedFilePath, StandardCopyOption.REPLACE_EXISTING);
            log("Immagine salvata nel percorso di deployment: " + deployedFilePath);
        }

        // --- 2. PERCORSO DEL CODICE SORGENTE (per la persistenza) ---
        // Approccio più robusto per trovare la root del progetto
        File projectRoot = new File(getServletContext().getRealPath("")).getParentFile().getParentFile();
        String sourcePathDir = projectRoot.getAbsolutePath() + File.separator + "src" + File.separator + "main" +
                File.separator + "webapp" + File.separator + "images" + File.separator + "products";

        File sourceUploadDir = new File(sourcePathDir);
        if (!sourceUploadDir.exists()) {
            sourceUploadDir.mkdirs();
        }

        // Pulisce i vecchi file anche in questa location
        cleanOldFiles(sourceUploadDir, productId);

        Path sourceFilePath = new File(sourceUploadDir, newFileName).toPath();

        // Copia il file appena salvato dalla cartella di deployment a quella del codice sorgente
        Files.copy(deployedFilePath, sourceFilePath, StandardCopyOption.REPLACE_EXISTING);
        log("Immagine copiata nel percorso del codice sorgente: " + sourceFilePath);
    }

    /**
     * Funzione helper per cancellare i vecchi file di un prodotto.
     */
    private void cleanOldFiles(File directory, int productId) {
        File[] oldFiles = directory.listFiles((dir, name) -> name.startsWith(productId + "."));
        if (oldFiles != null) {
            for (File file : oldFiles) {
                file.delete();
            }
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(gson.toJson(Map.of("success", success, "message", message)));
    }
}