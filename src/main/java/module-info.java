module com.example.socketmessangeraoop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socketmessangeraoop to javafx.fxml;
    exports com.example.socketmessangeraoop;
}