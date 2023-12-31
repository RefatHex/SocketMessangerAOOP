package com.example.socketmessangeraoop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Socket;

public class Main extends Application {
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChatBox.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("EasyNeeds");
        stage.setScene(scene);
        stage.show();
//        Client client = new Client(new Socket("localhost", 5555), "username","target_user");
        //username=data.username users username
        //targetName= cards userName. so the two usergets private channel between them
    }

    public static void main(String[] args) {
        launch(args);
    }

}