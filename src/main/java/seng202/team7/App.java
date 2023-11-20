package seng202.team7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Basic wrapper class for running FXML application
 * @author Morgan English
 */
public class App 
{
    private static final Logger log = LogManager.getLogger(App.class);
    /**
     * Method to run the application
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        log.info("Running application...");
        MainWindow.main(args);
    }
}
