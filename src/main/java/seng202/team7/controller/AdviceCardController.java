package seng202.team7.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.business.AdviceLogic;

import java.util.Objects;

/**
 * FXML controller class for an advice card. An advice card is a user-friendly way to inform the user about possible
 * risks in a given area or along a given route etc.
 * It can be loaded into the GUI multiple times, and also deleted from it.
 * @author Stephen Hockey
 */
public class AdviceCardController {

    private static final Logger log = LogManager.getLogger(AdviceCardController.class);

    private MainController mainController;
    @FXML
    private VBox root;
    @FXML
    private Label adviceLabel;
    @FXML
    private Label adviceCardTitle;
    @FXML
    private ImageView adviceImg;
    private Pane parentPane;

    /**
     * Initialises the advice card with all the relevant parameters.
     * Setting the parent pane allows the card to be deleted easily within this controller.
     *
     * @param stage stage to load
     * @param title the title of the advice card
     * @param advice the advice on the card
     * @param imageURL the url of the image for the given advice card
     * @param mainController the MainController that initialised the advice card and its controller.
     */
    public void init(Stage stage, String title, String advice, String imageURL, MainController mainController) {
        this.mainController = mainController;
        this.parentPane = (Pane) root.getParent();
        adviceLabel.setText(advice);
        adviceCardTitle.setText(title);
        String imageResource = Objects.requireNonNull(AdviceLogic.class.getResource(imageURL)).toString();
        Image image = new Image(imageResource);
        adviceImg.setImage(image);
    }

    /**
     * Deletes the card that this controller is controlling
     */
    @FXML
    public void deleteCard() {
        try {
            parentPane.getChildren().remove(root);
            mainController.adviceCardDeleted(this);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
