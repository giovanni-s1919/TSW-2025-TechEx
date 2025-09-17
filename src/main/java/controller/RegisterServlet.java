package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.UserDAO;
import model.dto.UserDTO;
import util.Utility;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet(name = "register", value = "/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        userDAO = new UserDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("islogin", false);
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String surname =  request.getParameter("surname");
        String birthDateStr = request.getParameter("birthDate");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String telephone = request.getParameter("phonenumber");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        String role = request.getParameter("role");

        try {
            if(userDAO.findByEmail(email) != null){
                request.setAttribute("errorMessage", "Email già registrata! Riprova.");
                request.setAttribute("islogin", false);
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore interno del server");
            request.setAttribute("islogin", false);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }
        try {
            if(userDAO.findByUsername(username) != null){
                request.setAttribute("errorMessage", "Questo username è già stato scelto! Riprova.");
                request.setAttribute("islogin", false);
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }
        } catch(SQLException e){
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore interno del server");
            request.setAttribute("islogin", false);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        LocalDate birthDate = null;
        try {
            if (birthDateStr == null || birthDateStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "La data di nascita è obbligatoria.");
                request.setAttribute("islogin", false);
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }
            birthDate = LocalDate.parse(birthDateStr);
            LocalDate today = LocalDate.now();
            LocalDate eighteenYearsAgo = today.minusYears(18);
            if (birthDate.isAfter(today)) {
                request.setAttribute("errorMessage", "La data di nascita non può essere una data futura!");
                request.setAttribute("islogin", false);
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }
            if (birthDate.isAfter(eighteenYearsAgo)) {
                request.setAttribute("errorMessage", "Devi avere almeno 18 anni per registrarti!");
                request.setAttribute("islogin", false);
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }
        } catch (DateTimeParseException e) {
            request.setAttribute("errorMessage", "Formato data di nascita non valido. Usa GG/MM/AAAA.");
            request.setAttribute("islogin", false);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        if (telephone != null && !telephone.trim().isEmpty()) {
            if (!telephone.matches("^[0-9+\\- ]{1,15}$")) {
                request.setAttribute("errorMessage", "Formato numero di telefono non valido.");
                request.setAttribute("islogin", false);
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }
        }

        boolean isLengthValid = password.length() >= 8;
        boolean hasEnoughUppercases = password.matches(".*[A-Z].*[A-Z].*");
        boolean hasEnoughLowercases = password.matches(".*[a-z].*[a-z].*");
        boolean hasSpecialChar = password.matches(".*[^a-zA-Z0-9].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean isPasswordValid = isLengthValid && hasEnoughUppercases && hasEnoughLowercases && hasSpecialChar && hasNumber;
        if (!isPasswordValid) {
            String policyMessage = "La password non rispetta i criteri di sicurezza (min. 8 caratteri, 2 maiuscole, 2 minuscole, 1 numero, 1 simbolo). Riprova.";
            request.setAttribute("errorMessage", policyMessage);
            request.setAttribute("islogin", false);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }
        if(!password.equals(confirm)){
            request.setAttribute("errorMessage", "Le password non corrispondono.");
            request.setAttribute("islogin", false);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        String passwordHash = Utility.hashPassword(password);
        UserDTO user = new UserDTO(name, surname, birthDate, username, email, passwordHash, telephone, UserDTO.Role.valueOf(role));
        try {
            userDAO.save(user);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore interno del server");
            request.setAttribute("islogin", false);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("login?action=login");
    }
}