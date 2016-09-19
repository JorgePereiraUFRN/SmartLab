package smartmetropolis.smartlab.MeasurementBlackBoard;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
    /** The servlet context with which we are associated. */
    private ServletContext context = null;

    public void contextDestroyed(ServletContextEvent event) {
        log("Context destroyed");
        this.context = null;
    }


    public void contextInitialized(ServletContextEvent event) {
        this.context = event.getServletContext();
        log("Context initialized");
        
        new MonitoreAirConditioner().start();
    }

    private void log(String message) {
        if (context != null) {
            context.log("MyServletContextListener: " + message);
        } else {
            System.out.println("MyServletContextListener: " + message);
        }
    }
}