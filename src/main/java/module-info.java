module com.example.tourlso {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;


    opens com.example.map to javafx.fxml;
    exports com.example.tourlso;
}