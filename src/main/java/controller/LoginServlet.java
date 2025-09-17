package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.Utility;
import model.dao.UserDAO;
import model.dto.UserDTO;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        userDAO = new UserDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        boolean isLogin = !"register".equalsIgnoreCase(action);
        request.setAttribute("islogin", isLogin);
        String successParam = request.getParameter("success");
        if ("true".equals(successParam)) {
            request.setAttribute("successMessage", "Registrazione avvenuta con successo! Ora puoi accedere.");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(email != null && !email.trim().isEmpty()) {
            try {
                UserDTO user = userDAO.findByEmail(email);
                if (user == null || !Utility.checkPassword(password, user.getPasswordHash())) {
                    request.setAttribute("errorMessage", "Email o password non validi, riprova.");
                    request.setAttribute("islogin", true);
                    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                    return;
                }
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/home");
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Errore interno del server");
                String action = request.getParameter("action");
                request.setAttribute("islogin", true);
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            }
        }
        else{
            try{
                UserDTO user = userDAO.findByUsername(username);
                if(user == null || !Utility.checkPassword(password, user.getPasswordHash())){
                    request.setAttribute("errorMessage", "Username o password non validi, riprova.");
                    request.setAttribute("islogin", true);
                    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                    return;
                }
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/home");
            } catch(SQLException e){
                e.printStackTrace();
                request.setAttribute("errorMessage", "Errore interno del server");
                String action = request.getParameter("action");
                request.setAttribute("islogin", true);
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            }
        }
    }
}
