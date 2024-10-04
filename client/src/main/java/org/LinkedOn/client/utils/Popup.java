package org.linkedon.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.linkedon.client.Client;

import java.io.IOException;

public class Popup {

    private static StackPane popupContainer;

    public static void showPopup(String fxmlFileName, Object controller, double width, double height) throws IOException {
        if (popupContainer != null) {
            ((StackPane) Client.getMainScene().getRoot()).getChildren().remove(popupContainer);
        }

        FXMLLoader loader = new FXMLLoader(Popup.class.getResource(STR."/org/linkedon/client/views/\{fxmlFileName}"));
        loader.setController(controller);
        AnchorPane popupRoot = loader.load();
        popupRoot.setMaxHeight(height);
        popupRoot.setMaxWidth(width);

        // Center the popup
        StackPane.setAlignment(popupRoot, Pos.CENTER);

        // Create a container for the popup
        popupContainer = new StackPane();
        popupContainer.setPrefWidth(Client.getMainScene().getWidth());
        popupContainer.setPrefHeight(Client.getMainScene().getHeight());

        // Create a semi-transparent dark layer
        Rectangle overlay = new Rectangle();
        overlay.setFill(Color.rgb(0, 0, 0, 0.5)); // 50% opacity
        overlay.widthProperty().bind(Client.getMainScene().widthProperty());
        overlay.heightProperty().bind(Client.getMainScene().heightProperty());

        // Add the overlay and popup to the container
        popupContainer.getChildren().addAll(overlay, popupRoot);

        // Add the popup container to the center of the BorderPane
        ((StackPane) Client.getMainScene().getRoot()).getChildren().add(popupContainer);
    }

    public static void hidePopup() {
        if (popupContainer != null) {
            ((StackPane) Client.getMainScene().getRoot()).getChildren().remove(popupContainer);
            popupContainer = null;
        }
    }

}
