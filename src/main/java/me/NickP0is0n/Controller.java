package me.NickP0is0n;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.Optional;

public class Controller {

    @FXML
    private ChoiceBox<String> taskSelector;

    @FXML
    private TextArea taskEdit;

    @FXML
    private TextArea t1in;

    @FXML
    private TextArea t1out;

    @FXML
    private TextArea t2in;

    @FXML
    private TextArea t2out;

    @FXML
    private TextArea t3in;

    @FXML
    private TextArea t3out;

    @FXML
    private TextArea t4in;

    @FXML
    private TextArea t4out;

    @FXML
    private TextArea t5in;

    @FXML
    private TextArea t5out;

    @FXML
    private Button saveTaskBtn;

    @FXML
    private TextField taskName;

    @FXML
    private Button addTaskBtn;

    private boolean hasFileSaved = false; //Чи збережено десь файл
    private boolean isFileInWork = false; //Чи відкрито якийсь файл
    private File currentFile; //Поточний файл у готовому до роботи вигляді
    private TaskSet currentTaskSet; //Поточний набір задач
    private int taskNumber = 0; //Кількість задач

    @FXML
    void initialize() {
        makeNewFile(new ActionEvent());
    }

    @FXML
    void addNewTask(ActionEvent event) {
        taskNumber++;
        currentTaskSet.add(new Task("Untitled task"));
        taskSelector.getItems().add(taskNumber + ". " + currentTaskSet.get(taskNumber - 1).getTaskName());
        taskSelector.setValue(taskNumber + ". " + currentTaskSet.get(taskNumber - 1).getTaskName());
    }

    @FXML
    void exitApp(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void makeNewFile(ActionEvent event) {
        isFileInWork = true;
        currentTaskSet = new TaskSet();
        setButtonsState(true);
        taskNumber = 0;
        taskSelector.setItems(FXCollections.observableArrayList());
        addNewTask(event);
    }

    @FXML
    void openFile(ActionEvent event) {
        FileChooser chooser = new FileChooser(); //діалог збереження
        chooser.setTitle("Choose task file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jTest task set files (.jt)", "*.jt")); //фильтр файлов
        File taskFile = chooser.showOpenDialog(new Stage()); //показ диалога на отдельной сцене
        if(taskFile != null)
        {
            hasFileSaved = true;
            currentFile = taskFile;
            setButtonsState(true);
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(taskFile)))
            {
                currentTaskSet = (TaskSet) ois.readObject();
            }
            catch(Exception ex){
                showError("An error occurred while reading the file!");
            }
            taskNumber = currentTaskSet.size();
            updateSelector(0);
        }
    }

    @FXML
    void saveAsFile(ActionEvent event) {
        if (isFileInWork) {
            FileChooser chooser = new FileChooser(); //діалог збереження
            chooser.setTitle("Save the task file");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jTest task set files (.jt)", "*.jt")); //фильтр файлов
            File taskFile = chooser.showSaveDialog(new Stage()); //показ диалога на отдельной сцене
            if (taskFile != null) {
                try {
                    taskFile.createNewFile();
                    hasFileSaved = true;
                    currentFile = taskFile;
                } catch (IOException e) {
                    hasFileSaved = false;
                    currentFile = null;
                    e.printStackTrace();
                    showError("An error occurred while creating the file!");
                }
            }
            if (currentFile != null) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentFile))) {
                    oos.writeObject(currentTaskSet);
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("An error occurred while saving the file!");
                }
            }
        }
    }

    @FXML
    void saveFile(ActionEvent event) {
        if (!hasFileSaved) saveAsFile(event);
        else
        {
            try {
                currentFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentFile))) {
                oos.writeObject(currentTaskSet);
            } catch (Exception e) {
                e.printStackTrace();
                showError("An error occurred while saving the file!");
            }
        }
    }

    @FXML
    void saveTask(ActionEvent event) {
        int selectedItem = getSelectedItemIndex(taskSelector.getValue());
        String[] tasksIn = new String[5];
        String[] tasksOut = new String[5];
        getTasks(tasksIn, t1in, t2in, t3in, t4in, t5in);
        getTasks(tasksOut, t1out, t2out, t3out, t4out, t5out);
        currentTaskSet.set(selectedItem, currentTaskSet.get(selectedItem).setTaskName(taskName.getText()).setTaskIn(tasksIn).setTaskOut(tasksOut).setTaskDescription(taskEdit.getText()));
        updateSelector(selectedItem);
    }

    private void getTasks(String[] tasksIn, TextArea t1in, TextArea t2in, TextArea t3in, TextArea t4in, TextArea t5in) {
        tasksIn[0] = t1in.getText();
        tasksIn[1] = t2in.getText();
        tasksIn[2] = t3in.getText();
        tasksIn[3] = t4in.getText();
        tasksIn[4] = t5in.getText();
    }

    @FXML
    void setPassword(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password protection");
        dialog.setContentText("Enter the password you want to protect the task file:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            if(result.get().isEmpty()) currentTaskSet.setPasswordProtected(false);
            else
            {
                currentTaskSet.setPassword(result.get());
                currentTaskSet.setPasswordProtected(true);
            }
        }
    }

    @FXML
    void about(ActionEvent event) throws IOException {
        FXMLLoader loader = Main.makeLoader("about.fxml");
        Stage stage = Main.startStage(loader, "About jTest Student", 600, 400, false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    void setButtonsState(boolean state) //Керування станом кнопок, які відповідають за зміни в задачах
    {
        taskSelector.setDisable(!state);
        addTaskBtn.setDisable(!state);
        saveTaskBtn.setDisable(!state);
    }

    void showError(String text)
    {
        Alert error = new Alert(Alert.AlertType.ERROR); //Створення повідомлення про помилку
        error.setTitle("Error");
        error.setContentText(text);
        error.showAndWait();
    }

    int getSelectedItemIndex(String item)
    {
        for (int i = 0; i < taskSelector.getItems().size(); i++)
        {
            if (taskSelector.getItems().get(i).equals(item)) return i;
        }
        return -1; //У випадку помилки
    }

    void updateSelector(int selectedItem)
    {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < currentTaskSet.size(); i++) items.add((i + 1) + ". " + currentTaskSet.get(i).getTaskName());
        taskSelector.setItems(items);
        taskSelector.setValue((selectedItem + 1) + ". " + currentTaskSet.get(selectedItem).getTaskName());
    }

}
