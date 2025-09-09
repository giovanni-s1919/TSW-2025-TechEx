package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.*;
import model.dto.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ProductServlet", value = {"/product"})
public class ProductServlet extends HttpServlet {
    private ProductDAO  productDAO;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private WishlistDAO wishlistDAO;
    private WishlistItemDAO wishlistItemDAO;


    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        productDAO = new ProductDAO(ds);
        cartItemDAO = new CartItemDAO(ds);
        cartDAO = new CartDAO(ds);
        wishlistDAO = new WishlistDAO(ds);
        wishlistItemDAO = new WishlistItemDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = null;
        int id=0;

        if(request.getParameter("idProduct") != null) {
            id = Integer.parseInt(request.getParameter("idProduct"));
        }

        if(session != null){
            user =  (UserDTO) session.getAttribute("user");
        }

        if(user == null) request.setAttribute("role", "Guest");
        else request.setAttribute("role", user.getRole().toString());

        try{
            ProductDTO product = null;
            if(id > 0){
                product = productDAO.findById(id);
            }
            if(product != null){
                request.setAttribute("product", product);
            }
            else {
                System.out.println("Il prodotto non è stato trovato");
                request.setAttribute("product", null);
            }
        }catch (SQLException e){
            e.printStackTrace();
            request.setAttribute("role", "Guest");
            request.setAttribute("isempty", true);
            request.setAttribute("error", "Errore interno del server");
            request.getRequestDispatcher("/WEB-INF/jsp/product.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/product.jsp").forward(request, response);
        return;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int productID =  Integer.parseInt(request.getParameter("idProduct"));
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");

        if(user==null){
            response.sendRedirect("login?action=login");
        }

        try {
            if("addToCart".equals(action)){
                CartDTO cart = cartDAO.findByUserID(user.getId());
                if(cart == null){
                    cart = new CartDTO();
                    cart.setUserID(user.getId());
                    cartDAO.save(cart);
                }
                else{
                    CartItemDTO test = cartItemDAO.findByProductIDAndCartID(cart.getId(), productID);
                    if(test != null){
                        System.out.println("test non è null");
                        test.setQuantity(test.getQuantity()+1);
                        cartItemDAO.update(test);
                        response.sendRedirect(request.getContextPath() + "/cart");
                        return;
                    }
                }
                CartItemDTO newItem = new CartItemDTO();
                newItem.setCartID(cart.getId());
                newItem.setProductID(productID);
                newItem.setQuantity(1);
                cartItemDAO.save(newItem);
                response.sendRedirect(request.getContextPath() + "/cart");
            }
            else if("addToWishlist".equals(action)){
                WishlistDTO wishlist =  wishlistDAO.findByUserID(user.getId());
                if(wishlist == null){
                    wishlist = new WishlistDTO();
                    wishlist.setUserID(user.getId());
                    wishlistDAO.save(wishlist);
                    response.sendRedirect(request.getContextPath() + "/wishlist");
                }
                else{
                    WishlistItemDTO test = wishlistItemDAO.findByWishlistAndProduct(wishlist.getId(), productID);
                    if(test != null){
                        System.out.println("Il prodotto è già presente nella lista desideri");
                        response.sendRedirect(request.getContextPath() + "/wishlist");
                        return;
                    }
                    WishlistItemDTO newItem = new WishlistItemDTO();
                    newItem.setProductID(productID);
                    newItem.setWishlistID(wishlist.getId());
                    wishlistItemDAO.save(newItem);
                    response.sendRedirect(request.getContextPath() + "/wishlist");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
