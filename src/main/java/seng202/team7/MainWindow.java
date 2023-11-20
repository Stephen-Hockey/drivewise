package seng202.team7;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.controller.MainController;
import seng202.team7.controller.SplashController;

import java.io.IOException;
import java.util.Objects;

/**
 * This class handles the launching of the program alongside App.java
 * @author Bella Hill
 */
public class MainWindow extends Application {
    private static final Logger log = LogManager.getLogger(MainWindow.class);

    /**
     * Overrides start to show a splash screen with the DriveWise logo.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        showSplashScreen(primaryStage);
    }

    /**
     * Displays the DriveWise splash screen briefly on launch of the application
     * @param primaryStage The stage upon which the main application will run
     */
    private void showSplashScreen(Stage primaryStage) {
        try {
            FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/fxml/splash.fxml"));
            Parent splashParent = splashLoader.load();

            Stage splashStage = new Stage();
            splashStage.initStyle(StageStyle.UNDECORATED);

            Scene splashScene = new Scene(splashParent);
            splashStage.setScene(splashScene);

            SplashController splashController = splashLoader.getController();
            splashStage.show();

            // Create a Service to perform background initialization
            BackgroundInitService backgroundInitService = new BackgroundInitService();
            backgroundInitService.setOnSucceeded(event -> {
                showMainApplication(primaryStage);
                splashStage.close();
            });

            // Add a fade transition to smoothly fade in the splash screen
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2.5), splashParent);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();

            // Start the background service
            backgroundInitService.start();

        } catch (IOException e) {
            log.error(e.getMessage());

        }
    }

    /**
     * Displays the main application.
     * @param primaryStage the main stage
     */
    private void showMainApplication(Stage primaryStage) {
        try {
            FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent mainWindowParent = mainWindowLoader.load();

            MainController mainController = mainWindowLoader.getController();
            mainController.init(primaryStage);
            Scene scene = new Scene(mainWindowParent);

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            // Set the stage size to the screen's dimensions
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());

            //primaryStage.setScene(scene);
            primaryStage.show();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheets/main.css")).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("/img/logo.png"));
            primaryStage.setTitle("DriveWise");
            //primaryStage.sizeToScene();
            primaryStage.setFullScreen(true);
            primaryStage.show();


        } catch (IOException e) {
            log.error(e.getMessage());

        }
    }

    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * A service for background initialization tasks.
     * This service is used to perform background tasks that require initialization.
     */
    private static class BackgroundInitService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    // Simulate long initialization
                    Thread.sleep(3000);
                    return null;
                }
            };
        }
    }
}
