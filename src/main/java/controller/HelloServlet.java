package controller;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.dao.UserDAO;
import model.dto.UserDTO;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        ServletContext context = getServletContext();
        UserDAO userDAO = new UserDAO(context);
        List<UserDTO> list = null;

        try{
            list = userDAO.findAll("ID");
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("errore.");
        }

        System.out.println("DataSource in servlet: " + getServletContext().getAttribute("datasource"));
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println(list);
        out.println("</body></html>");
    }

    public void destroy() {
    }
}