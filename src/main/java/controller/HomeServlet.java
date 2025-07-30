package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.UserDTO;

import java.io.IOException;

// maps the servlet to the root URL ("/") and "/home"
@WebServlet(name = "HomeServlet",  value = {"", "/home"})
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //TODO
        HttpSession session = request.getSession(false);
        if(session!=null){
            UserDTO  user = (UserDTO)session.getAttribute("user");
            if(user != null){
                request.setAttribute("role", user.getRole());
            }
            else{
                request.setAttribute("role", "Guest");
            }
        }
        else{
            request.setAttribute("role", "Guest");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/home.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //  home page doesn't handle POST requests
        doGet(request,response);
    }
}
