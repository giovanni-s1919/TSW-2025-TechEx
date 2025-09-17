package util.task;

import util.task.OrderUpdateTask;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AppLifecycleListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Questo metodo viene chiamato quando l'applicazione parte
        ServletContext context = sce.getServletContext();
        DataSource dataSource = (DataSource) context.getAttribute("datasource");

        if (dataSource == null) {
            System.err.println("DataSource non trovato, impossibile avviare il task di aggiornamento ordini.");
            return;
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new OrderUpdateTask(dataSource);

        // Schedula il task per essere eseguito ogni minuto, partendo subito.
        // Parametri: task, ritardo iniziale, periodo, unità di tempo
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);

        System.out.println("Task di aggiornamento stato ordini schedulato con successo.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Questo metodo viene chiamato quando l'applicazione si ferma (es. shutdown di Tomcat)
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                // Attendi fino a 10 secondi per la terminazione dei task in esecuzione
                if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Il task di aggiornamento non è terminato entro 10 secondi, forzo la chiusura.");
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
        System.out.println("Scheduler per aggiornamento ordini terminato.");
    }
}