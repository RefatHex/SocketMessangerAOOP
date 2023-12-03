module com.example.socketmessangeraoop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.socketmessangeraoop to javafx.fxml;
    exports com.example.socketmessangeraoop;
}