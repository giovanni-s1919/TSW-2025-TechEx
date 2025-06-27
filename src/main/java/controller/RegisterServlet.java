package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.UserDAO;
import model.dto.UserDTO;
import model.Utility;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "register", value = "/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        userDAO = new UserDAO(ds);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        try {
            if(userDAO.findByEmail(email) != null){
                request.setAttribute("errorMessage", "Email già registrata.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore interno del server.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if(!password.equals(confirm)){
            request.setAttribute("errorMessage", "Le password non corrispondono.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        String passwordHash = Utility.hashPassword(password);

        UserDTO user = new UserDTO(username, email, passwordHash, UserDTO.Role.valueOf("Customer"));
        try {
            userDAO.save(user);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore interno del server.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("login.jsp");
    }
}
