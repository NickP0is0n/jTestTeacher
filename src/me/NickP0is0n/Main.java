package me.NickP0is0n;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import javax.swing.*;
import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("jTestTeacher.fxml"));
        primaryStage.setTitle("jTest Teacher");
        primaryStage.setScene(new Scene(root, 640, 581));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(new File("/resources/logo.png").toURI().toString()));
        com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon("resources/logo.png").getImage()); //для иконки в доке macOS
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
