package com.example.tourlso;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class HelloApplication extends Application {

    private final Map<String, String> placeMap = new LinkedHashMap<>();
    private WebEngine webEngine;
    private VBox optionsBox;
    private final MediaController mediaController = new MediaController();

    @Override
    public void start(Stage primaryStage) {
        // Define valid places and coordinates
        placeMap.put("Katse Dam", "-29.2786,28.5167");
        placeMap.put("Maletsunyane Fall", "-29.8356,28.1361");
        placeMap.put("Thaba Bosiu", "-29.4534,27.7225");
        placeMap.put("Metolong", "-29.4444,27.9556");
        placeMap.put("Afriski", "-28.8228,28.7322");

        // Web view setup
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        loadMap("-29.6099,28.2336"); // Default center

        // Dropdown ComboBox for places
        Label searchLabel = new Label("Select Place:");
        ComboBox<String> placeDropdown = new ComboBox<>();
        placeDropdown.getItems().addAll(placeMap.keySet());
        placeDropdown.setPromptText("Choose a destination");

        // Buttons
        Button searchBtn = new Button("Search");
        Button btnVideo = new Button("Watch Video");
        Button btnAudio = new Button("Play Audio");
        Button btnQuiz = new Button("Take Quiz");

        // New button styles (Cool blue theme)
        String searchStyle = "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-padding: 10px 20px; -fx-background-radius: 8px;";
        String videoStyle = "-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-padding: 10px 20px; -fx-background-radius: 8px;";
        String audioStyle = "-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-padding: 10px 20px; -fx-background-radius: 8px;";
        String quizStyle = "-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-size: 14px; "
                + "-fx-padding: 10px 20px; -fx-background-radius: 8px;";

        searchBtn.setStyle(searchStyle);
        btnVideo.setStyle(videoStyle);
        btnAudio.setStyle(audioStyle);
        btnQuiz.setStyle(quizStyle);

        // Options box
        optionsBox = new VBox(10, btnVideo, btnAudio, btnQuiz);
        optionsBox.setPadding(new Insets(10));
        optionsBox.setVisible(false);

        VBox sidebar = new VBox(15, searchLabel, placeDropdown, searchBtn, optionsBox);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);
        sidebar.setAlignment(Pos.TOP_CENTER);

        // Sidebar styling
        sidebar.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #0F2027, #203A43, #2C5364);
            -fx-border-color: #00BFFF;
            -fx-border-width: 2px;
            -fx-border-radius: 12px;
            -fx-background-radius: 12px;
            -fx-effect: dropshadow(gaussian, rgba(0,191,255,0.4), 12, 0, 2, 2);
        """);

        searchLabel.setStyle("-fx-text-fill: #00BFFF; -fx-font-size: 16px; -fx-font-weight: bold;");
        placeDropdown.setStyle("""
            -fx-background-color: #333333;
            -fx-text-fill: #00BFFF;
            -fx-border-color: #00BFFF;
            -fx-prompt-text-fill: #00BFFF;
        """);

        // Button event handlers
        searchBtn.setOnAction(e -> {
            String selectedPlace = placeDropdown.getValue();
            if (selectedPlace != null && placeMap.containsKey(selectedPlace)) {
                String coords = placeMap.get(selectedPlace);
                loadMap(coords);
                optionsBox.setVisible(true);
                mediaController.setCurrentPlace(selectedPlace);
            } else {
                showInfoDialog("Invalid Selection", "Please select a valid place from the dropdown.");
                optionsBox.setVisible(false);
            }
        });

        btnVideo.setOnAction(e -> mediaController.showVideoDialog());
        btnAudio.setOnAction(e -> mediaController.showAudioDialog());
        btnQuiz.setOnAction(e -> mediaController.showQuizDialog());

        HBox layout = new HBox(sidebar, webView);
        HBox.setHgrow(webView, Priority.ALWAYS);

        Scene scene = new Scene(layout, 1000, 600);
        primaryStage.setTitle("Lesotho Map Tour Guide");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadMap(String latLng) {
        String embedHTML = """
            <!DOCTYPE html>
            <html>
              <body style="margin:0; padding:0;">
                <iframe 
                  src="https://maps.google.com/maps?q=%s&z=13&output=embed"
                  width="100%%" 
                  height="100%%" 
                  frameborder="0" 
                  style="border:0;">
                </iframe>
              </body>
            </html>
            """.formatted(latLng);
        webEngine.loadContent(embedHTML);
    }

    private void showInfoDialog(String title, String message) {
        Stage dialog = new Stage();
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.CENTER);

        Label msgLabel = new Label(message);
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> dialog.close());

        content.getChildren().addAll(msgLabel, closeBtn);

        Scene scene = new Scene(content, 300, 150);
        dialog.setTitle(title);
        dialog.setScene(scene);
        dialog.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
