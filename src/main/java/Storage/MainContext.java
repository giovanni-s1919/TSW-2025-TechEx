package Storage;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MainContext implements ServletContextListener {
    public void  contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        DataSource ds = null;

        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TechEx");
        } catch (NamingException e){
            System.out.println("Error: " + e.getMessage());
        }

        context.setAttribute("datasource", ds);
        System.out.println("DataSource creation..." + ds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
