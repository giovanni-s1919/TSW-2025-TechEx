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
import model.view.CartDisplayItem;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", value = {"/cart"})
public class CartServlet extends HttpServlet {
    private CartDAO cartDAO;
    CartItemDAO cartItemDAO;
    ProductDAO productDAO;
    @Override
    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        cartDAO = new CartDAO(ds);
        cartItemDAO = new CartItemDAO(ds);
         productDAO = new ProductDAO(ds);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = null;
        if(session != null){
            user  = (UserDTO) session.getAttribute("user");
        }
        if(user == null){
            request.setAttribute("role", "Guest");
            request.setAttribute("cartItems", new  ArrayList<CartItemDTO>());
            request.setAttribute("cartTotal", 0);
            request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(request, response);
            return;
        }

        request.setAttribute("role", user.getRole());

        List<CartDisplayItem> cartDisplayItems = new ArrayList<>();
        Float cartTotal = 0.0f;

        try {
            CartDTO cart = cartDAO.findByUserID(user.getId());

            if(cart != null){
                List<CartItemDTO> cartItems = cartItemDAO.findByCartID(cart.getId());

                for(CartItemDTO cartItem : cartItems){
                    ProductDTO product = productDAO.findById(cartItem.getProductID());

                    if(product != null){
                        System.out.println("Product non è null");
                        CartDisplayItem displayItem = new CartDisplayItem(product,cartItem.getQuantity());
                        cartDisplayItems.add(displayItem);
                        cartTotal = cartTotal+(product.getPrice()*cartItem.getQuantity());
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            request.setAttribute("cartItems", new  ArrayList<CartItemDTO>());
            request.setAttribute("cartTotal", 0);
            request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(request, response);
            return;
        }

        request.setAttribute("cartItems", cartDisplayItems);
        request.setAttribute("cartTotal", cartTotal);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp");
        dispatcher.forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        UserDTO user = null;
//        if(session != null) {
//            user = (UserDTO) session.getAttribute("user");
//        }
//
//        if(user==null) {
//            request.setAttribute("role", "Guest");
//            request.setAttribute("cartItems", new ArrayList<>()); // Lista vuota
//            request.setAttribute("cartTotal", 0);
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp");
//            dispatcher.forward(request, response);
//            return;
//        }
//        request.setAttribute("role",  user.getRole());
//        List<CartDisplayItem> cartDispayItems = new ArrayList<>();
//        Float cartTotal = 0.0f;
//
//        try{
//            CartDTO cart = cartDAO.findByUserID(user.getId());
//
//            if(cart!=null){
//                List<CartItemDTO> cartItems = cartItemDAO.findByCartID(cart.getId());
//                for(CartItemDTO item : cartItems){
//                    ProductDTO product = productDAO.findById(item.getProductID());
//                    if(product!=null){
//                        CartDisplayItem displayItem = new CartDisplayItem(product,item.getQuantity());
//                        cartDispayItems.add(displayItem);
//                        cartTotal += displayItem.getSubTotal();
//
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            throw new ServletException("Errore durante il caricamento del carrello");
//        }
//        request.setAttribute("cartItems",cartDispayItems);
//        request.setAttribute("cartTotal", cartTotal);
//
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp");
//        dispatcher.forward(request, response);
//    }