package me.NickP0is0n;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("jTestTeacher.fxml"));
        primaryStage.setTitle("jTest Teacher");
        primaryStage.setScene(new Scene(root, 640, 581));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("logo.png")));
        java.awt.Image logo = SwingFXUtils.fromFXImage(new Image(Main.class.getClassLoader().getResourceAsStream("logo.png")), null);
        System.out.println();
        if(System.getProperty("os.name").equals("Mac OS X")) com.apple.eawt.Application.getApplication().setDockIconImage(logo); //для иконки в доке macOS
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
