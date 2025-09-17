package util.task;

import model.dao.OrderDAO;
import javax.sql.DataSource;
import java.sql.SQLException;

public class OrderUpdateTask implements Runnable {

    private final OrderDAO orderDAO;

    public OrderUpdateTask(DataSource dataSource) {
        this.orderDAO = new OrderDAO(dataSource);
    }

    @Override
    public void run() {
        try {
            System.out.println("Esecuzione task di aggiornamento stato ordini...");
            orderDAO.updateOrderStatusByAge();
            System.out.println("Task di aggiornamento completato.");
        } catch (SQLException e) {
            // È importante gestire le eccezioni qui per evitare che il thread schedulato muoia
            System.err.println("Errore durante l'aggiornamento automatico dello stato degli ordini: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Errore imprevisto nel task di aggiornamento ordini: " + e.getMessage());
            e.printStackTrace();
        }
    }
}