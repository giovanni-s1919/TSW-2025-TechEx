package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ProductDAO;
import model.dto.ProductDTO;
import model.dto.UserDTO;
import model.view.ProductCardDisplay;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// maps the servlet to the root URL ("/") and "/home"
@WebServlet(name = "HomeServlet",  value = {"", "/home"})
public class HomeServlet extends HttpServlet {
    ProductDAO productDAO;
    List<ProductDTO> productList;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        productDAO =  new ProductDAO(ds);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        List<ProductCardDisplay> products = new  ArrayList<>();
        try {
            List<ProductDTO> productDTOS = productDAO.findAll("ID");
            int elements = 0;

            for (ProductDTO productDTO : productDTOS) {
                if(elements == 7) break;
                ProductCardDisplay product = new ProductCardDisplay(productDTO);
                products.add(product);
                elements++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        request.setAttribute("products", products);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/home.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
