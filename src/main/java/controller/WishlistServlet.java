package controller;

import com.sun.net.httpserver.Request;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.*;
import model.dto.*;
import model.view.WishlistDisplayItem;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="WishlistServlet", value = {"/wishlist"})
public class WishlistServlet extends HttpServlet {
    private WishlistDAO wishlistDAO;
    WishlistItemDAO wishlistItemDAO;
    ProductDAO productDAO;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;

    @Override
    public void init() {
        DataSource  ds = (DataSource) getServletContext().getAttribute("datasource");
        wishlistDAO = new WishlistDAO(ds);
        wishlistItemDAO = new WishlistItemDAO(ds);
        productDAO = new ProductDAO(ds);
        cartDAO = new CartDAO(ds);
        cartItemDAO = new CartItemDAO(ds);
        System.out.println("WishlistServlet init");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = null;

        if(session!=null){
            user = (UserDTO)session.getAttribute("user");
        }
        if(user==null){
            request.setAttribute("role", "Guest");
            request.setAttribute("wishlistItems", new ArrayList<WishlistItemDTO>());
            request.getRequestDispatcher("/WEB-INF/jsp/wishlist.jsp").forward(request, response);
            return;
        }
        request.setAttribute("role", user.getRole());
        List<WishlistDisplayItem> displayItems = new ArrayList<>();

        try{
            WishlistDTO wishlist = wishlistDAO.findByUserID(user.getId());
            if(wishlist!=null){
                List<WishlistItemDTO> wishlistItems = wishlistItemDAO.findByWishlistID(wishlist.getId());
                for(WishlistItemDTO wishlistItem : wishlistItems){
                    ProductDTO product = productDAO.findById(wishlistItem.getProductID());
                    if(product!=null){
                        WishlistDisplayItem displayItem = new WishlistDisplayItem(product);
                        displayItems.add(displayItem);
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            request.setAttribute("wishlistItems", new ArrayList<WishlistItemDTO>());
            request.getRequestDispatcher("/WEB-INF/jsp/wishlist.jsp");
            return;
        }
        request.setAttribute("displayItems", displayItems);
        RequestDispatcher dispatcher =  request.getRequestDispatcher("/WEB-INF/jsp/wishlist.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        int productId = 0;
        try {
            System.out.println("ID Prodotto: "+request.getParameter("idProduct"));
            productId = Integer.parseInt(request.getParameter("idProduct"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID prodotto non valido");
            return;
        }

        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            if ("addToCart".equals(action)) {
                CartDTO cart = cartDAO.findByUserID(user.getId());
                if (cart == null) {
                    cart = new CartDTO();
                    cart.setUserID(user.getId());
                    cartDAO.save(cart);
                }else {
                    CartItemDTO test = cartItemDAO.findByProductIDAndCartID(cart.getId(), productId);
                    if(test!=null){
                        test.setQuantity(test.getQuantity()+1);
                        cartItemDAO.update(test);
                        response.sendRedirect(request.getContextPath() + "/cart");
                        return;
                    }
                }
                CartItemDTO newItem = new CartItemDTO();
                newItem.setCartID(cart.getId());
                newItem.setProductID(productId);
                newItem.setQuantity(1);
                cartItemDAO.save(newItem);

                response.sendRedirect(request.getContextPath() + "/cart");

            } else if ("removeFromWishlist".equals(action)) {
                WishlistDTO wishlist = wishlistDAO.findByUserID(user.getId());
                if (wishlist != null) {
                    WishlistItemDTO itemToRemove = wishlistItemDAO.findByWishlistAndProduct(wishlist.getId(), productId);
                    if (itemToRemove != null) {
                        wishlistItemDAO.delete(itemToRemove.getId());
                    }
                }
                response.sendRedirect(request.getContextPath() + "/wishlist");
                return;
            }

        } catch (SQLException e) {
            throw new ServletException("Errore Database durante l'azione sulla wishlist", e);
        }
    }
}
