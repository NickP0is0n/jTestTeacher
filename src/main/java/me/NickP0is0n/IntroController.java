package me.NickP0is0n;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IntroController {

    @FXML
    private ListView<String> lastFileView;

    @FXML
    void initialize() throws IOException {
        File latestList = new File("last.list");
        if (!latestList.exists()) latestList.createNewFile();
        Scanner listScan = new Scanner(latestList);
        List<String> latestFilePaths = new ArrayList<>();
        while (listScan.hasNextLine()) latestFilePaths.add(listScan.nextLine());
        lastFileView.setItems(FXCollections.observableArrayList(latestFilePaths));
    }

    @FXML
    void onAboutBtn(ActionEvent event) throws IOException {
        FXMLLoader loader = Main.makeLoader("about.fxml");
        Stage stage = Main.startStage(loader, "About jTest Teacher", 600, 400, false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    @FXML
    void onExitBtn(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onOpenBtn(ActionEvent event) {

    }

    @FXML
    void onTutorBtn(ActionEvent event) {

    }

}
