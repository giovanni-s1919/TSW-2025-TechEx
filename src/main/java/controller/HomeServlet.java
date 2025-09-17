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
import java.util.Comparator;
import java.util.stream.Collectors;

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
                request.setAttribute("role", user.getRole().name());
            }
            else{
                request.setAttribute("role", "Guest");
            }
        }
        else{
            request.setAttribute("role", "Guest");
        }
        try {
            List<ProductDTO> productDTOS = productDAO.findAll("ID");
            List<ProductCardDisplay> products = new  ArrayList<>();
            int elements = 0;
            for (ProductDTO productDTO : productDTOS) {
                if(elements == 6) break;
                ProductCardDisplay product = new ProductCardDisplay(productDTO);
                products.add(product);
                elements++;
            }
            request.setAttribute("products", products);
            List<ProductDTO> topPricedProducts = productDTOS.stream()
                    .filter(p -> p.getGrade() == ProductDTO.Grade.Original)
                    .sorted(Comparator.comparing(ProductDTO::getPrice).reversed())
                    .limit(6)
                    .sorted(Comparator.comparing(ProductDTO::getName))
                    .collect(Collectors.toList());
            List<ProductCardDisplay> eliteProducts = new ArrayList<>();
            for (ProductDTO productDTO : topPricedProducts) {
                eliteProducts.add(new ProductCardDisplay(productDTO));
            }
            request.setAttribute("eliteProducts", eliteProducts);
            List<ProductCardDisplay> bestSellerProducts = new ArrayList<>();
            productDTOS.stream()
                    .filter(p -> p.getId() >= 31 && p.getId() <= 36)
                    .sorted(Comparator.comparing(ProductDTO::getName))
                    .forEach(productDTO -> {
                        bestSellerProducts.add(new ProductCardDisplay(productDTO));
                    });
            request.setAttribute("bestSellerProducts", bestSellerProducts);
            request.setAttribute("categoriesForHeader", ProductDTO.Category.values());
            request.setAttribute("brandsForHeader", productDAO.findDistinctBrands());
            request.setAttribute("gradesForHeader", ProductDTO.Grade.values());
        } catch (SQLException e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile caricare i dati della pagina.");
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/home.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
