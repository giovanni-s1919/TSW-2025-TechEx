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

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ProductServlet", value = {"/product"})
public class ProductServlet extends HttpServlet {
    private ProductDAO  productDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        productDAO = new ProductDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = null;
        int id = Integer.parseInt(request.getParameter("idProduct"));
        System.out.println("id prodotto: " + id);
        if(session != null){
            user =  (UserDTO) session.getAttribute("user");
        }
        if(user == null) request.setAttribute("role", "Guest");
        else request.setAttribute("role", user.getRole());
        try{
            System.out.println("Ricerca avviata");
            ProductDTO product = null;
            if(id > 0){
                product = productDAO.findById(id);
            }
            System.out.println("Ricerca completata");
            if(product != null){
                System.out.println("Il prodotto è stato trovato");
                request.setAttribute("product-name", product.getName());
                System.out.println(product.getName());
                request.setAttribute("product-description", product.getDescription());
                request.setAttribute("product-brand", product.getBrand());
                request.setAttribute("product-price", product.getPrice());
                request.setAttribute("product-category", product.getCategory());
                request.setAttribute("product-grade", product.getGrade());
                request.setAttribute("product-quantity", product.getStockQuantity());
            }
            else {
                System.out.println("Il prodotto non è stato trovato");
                request.setAttribute("isempty", true);
            }
        }catch (SQLException e){
            e.printStackTrace();
            request.setAttribute("role", "Guest");
            request.setAttribute("isempty", true);
            request.getRequestDispatcher("/WEB-INF/jsp/product.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/product.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
