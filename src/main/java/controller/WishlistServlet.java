package controller;

import com.sun.net.httpserver.Request;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ProductDAO;
import model.dao.WishlistDAO;
import model.dao.WishlistItemDAO;
import model.dto.ProductDTO;
import model.dto.UserDTO;
import model.dto.WishlistDTO;
import model.dto.WishlistItemDTO;
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

    @Override
    public void init() {
        DataSource  ds = (DataSource) getServletContext().getAttribute("datasource");
        wishlistDAO = new WishlistDAO(ds);
        wishlistItemDAO = new WishlistItemDAO(ds);
        productDAO = new ProductDAO(ds);
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
