package me.NickP0is0n;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class IntroController {

    @FXML
    private ListView<String> lastFileView;

    private void updateLatestList() throws IOException {
        File latestList = new File("last.list");
        if (!latestList.exists()) latestList.createNewFile();
        Scanner listScan = new Scanner(latestList);
        List<String> latestFilePaths = new ArrayList<>();
        while (listScan.hasNextLine()) latestFilePaths.add(listScan.nextLine());
        Collections.reverse(latestFilePaths);
        lastFileView.setItems(FXCollections.observableArrayList(latestFilePaths));
    }


    @FXML
    public void initialize() throws IOException {
        updateLatestList();
    }

    @FXML
    public void onListClick(MouseEvent event) throws IOException {
        String selectedItem = lastFileView.getSelectionModel().getSelectedItem();
        if (!selectedItem.equals("")) {
            FXMLLoader loader = Main.makeLoader("jTestTeacher.fxml");
            File selectedFile = new File(selectedItem);
            Stage stage = Main.startStage(loader, selectedFile.getAbsolutePath() + " [jTest Teacher]", 822, 480, false);
            Controller mainController = loader.getController();
            mainController.openFile(selectedFile);
            initialize();
            stage.show();
        }
    }

    @FXML
    public void onAboutBtn(ActionEvent event) throws IOException {
        FXMLLoader loader = Main.makeLoader("about.fxml");
        Stage stage = Main.startStage(loader, "About jTest Teacher", 600, 400, false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }


    @FXML
    public void onCreateBtn(ActionEvent event) throws IOException {
        FXMLLoader loader = Main.makeLoader("jTestTeacher.fxml");
        Stage stage = Main.startStage(loader, "Unnamed [jTest Teacher]", 822, 480, false);
        Controller mainController = loader.getController();
        mainController.makeNewFile();
        stage.showAndWait();
        updateLatestList();
    }

    @FXML
    public void onExitBtn(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void onOpenBtn(ActionEvent event) throws IOException {
        FileChooser chooser = new FileChooser(); //діалог збереження
        chooser.setTitle("Choose task file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jTest task set files (.jt)", "*.jt")); //фильтр файлов
        File taskFile = chooser.showOpenDialog(new Stage()); //показ диалога на отдельной сцене
        if(taskFile != null)
        {
            FXMLLoader loader = Main.makeLoader("jTestTeacher.fxml");
            Stage stage = Main.startStage(loader, taskFile.getAbsolutePath() + " [jTest Teacher]", 822, 480, false);
            Controller mainController = loader.getController();
            mainController.openFile(taskFile);
            updateLatestList();
            stage.show();
        }
    }

    @FXML
    public void onTutorBtn() {

    }

}
