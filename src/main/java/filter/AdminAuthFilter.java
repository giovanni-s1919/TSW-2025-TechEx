package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.UserDTO;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user != null && "Admin".equals(user.getRole().name())) {
            chain.doFilter(request, response);
        } else {
            System.out.println("Accesso non autorizzato all'area admin bloccato.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=unauthorized");
        }
    }
}