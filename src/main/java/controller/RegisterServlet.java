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

        // TODO
        // For example, checking if a user is already logged in
        request.setAttribute("islogin", false);
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String surname =  request.getParameter("surname");
        LocalDate  birthDate = LocalDate.parse(request.getParameter("birthDate"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        String role = request.getParameter("role");

        try {
            if(userDAO.findByEmail(email) != null){
                request.setAttribute("errorMessage", "Email già registrata");
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
                request.setAttribute("errorMessage", "Questo username è già stato scelto");
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

        if(!password.equals(confirm)){
            request.setAttribute("errorMessage", "Le password non corrispondono");
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




