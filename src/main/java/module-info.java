module com.tenzen.javachess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.tenzen.javachess to javafx.fxml;
    exports com.tenzen.javachess;
}