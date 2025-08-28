package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import util.Utility;

import model.dao.UserDAO;
import model.dto.UserDTO;



@WebServlet(name = "PersonalAreaServlet", value = {"/personal_area"})
public class PersonalAreaServlet extends HttpServlet {

    private UserDAO userDAO;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            DataSource dataSource = (DataSource) getServletContext().getAttribute("datasource");
            if (dataSource == null) {
                throw new ServletException("DataSource non disponibile nel contesto della servlet.");
            }
            userDAO = new UserDAO(dataSource);
        } catch (ServletException e) {
            log("Errore durante l'inizializzazione del UserDAO", e);
            throw e;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO loggedInUser = (UserDTO) session.getAttribute("user");

        if (session == null || loggedInUser == null || loggedInUser.getId() == 0) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
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
                            currentUser.setBirthDate(LocalDate.parse(value));
                            successMessage = "Data di nascita aggiornata con successo.";
                            fieldUpdated = true;
                        } catch (java.time.format.DateTimeParseException e) {
                            sendJsonResponse(response, false, "Formato data non valido (YYYY-MM-DD).", HttpServletResponse.SC_BAD_REQUEST);
                            return;
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

            if (newPassword.length() < 8) { // Esempio
                sendJsonResponse(response, false, "La nuova password deve contenere almeno 8 caratteri.", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                if (!Utility.checkPassword(currentPassword, currentUser.getPasswordHash())) {
                    sendJsonResponse(response, false, "La password attuale non è corretta.", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                String newPasswordHash = Utility.hashPassword(newPassword); // Assicurati che Utility.hashPassword esista

                currentUser.setPasswordHash(newPasswordHash);
                userDAO.update(currentUser);

                // Non aggiornare la sessione con la nuova password in chiaro!
                // La sessione contiene già l'hash, non c'è bisogno di ricaricarlo per la sessione.

                sendJsonResponse(response, true, "Password aggiornata con successo.", HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                log("Errore del database durante il cambio password per l'utente " + currentUser.getId(), e);
                sendJsonResponse(response, false, "Errore del database durante il cambio password.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) { // Catch per Utility.hashPassword se lancia eccezioni
                log("Errore durante l l'hashing della nuova password.", e);
                sendJsonResponse(response, false, "Errore interno durante il cambio password.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
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