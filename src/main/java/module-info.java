module com.eee {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.eee to javafx.fxml;
    exports com.eee;
}