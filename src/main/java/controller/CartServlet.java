package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CartDAO;
import model.dao.CartItemDAO;
import model.dao.ProductDAO;
import model.dto.CartDTO;
import model.dto.CartItemDTO;
import model.dto.ProductDTO;
import model.dto.UserDTO;
import model.view.CartDispayItem;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", value = {"/cart"})
public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = null;
        if(session != null) {
            user = (UserDTO) session.getAttribute("user");
        }

        if(user==null) {
            request.setAttribute("role", "Guest");
            request.setAttribute("cartItems", new ArrayList<>()); // Lista vuota
            request.setAttribute("cartTotal", 0);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp");
            dispatcher.forward(request, response);
            return;
        }
        request.setAttribute("role",  user.getRole());
        DataSource ds = (DataSource) request.getServletContext().getAttribute("datasource");
        CartDAO  cartDAO = new CartDAO(ds);
        CartItemDAO cartItemDAO = new CartItemDAO(ds);
        ProductDAO productDAO = new ProductDAO(ds);
        List<CartDispayItem> cartDispayItems = new ArrayList<>();
        Float cartTotal = 0.0f;

        try{
            CartDTO cart = cartDAO.findByUserID(user.getId());

            if(cart!=null){
                List<CartItemDTO> cartItems = cartItemDAO.findByCartID(cart.getId());
                for(CartItemDTO item : cartItems){
                    ProductDTO product = productDAO.findById(item.getProductID());
                    if(product!=null){
                        CartDispayItem displayItem = new CartDispayItem(product,item.getQuantity());
                        cartDispayItems.add(displayItem);
                        cartTotal += displayItem.getSubTotal();

                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Errore durante il caricamento del carrello");
        }
        request.setAttribute("cartItems",cartDispayItems);
        request.setAttribute("cartTotal", cartTotal);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp");
        dispatcher.forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
