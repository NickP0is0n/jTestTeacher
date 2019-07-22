package me.NickP0is0n;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.Optional;

@SuppressWarnings("ALL")
public class Controller {

    @FXML
    private ListView<String> taskSelector;

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

    @FXML
    private Button removeBtn;

    private boolean hasFileSaved = false; //Чи збережено десь файл
    private boolean isFileInWork = false; //Чи відкрито якийсь файл
    private File currentFile; //Поточний файл у готовому до роботи вигляді
    private TaskSet currentTaskSet; //Поточний набір задач
    private int taskNumber = 0; //Кількість задач

    @FXML
    public void initialize() {
        makeNewFile();
    }

    @FXML
    public void addNewTask() {
        taskNumber++;
        currentTaskSet.add(new Task("Untitled task"));
        taskSelector.getItems().add(currentTaskSet.get(taskNumber - 1).getTaskName());
        taskSelector.getSelectionModel().select(currentTaskSet.get(taskNumber - 1).getTaskName());
    }

    @FXML
    public void exitApp() {
        System.exit(0);
    }

    @FXML
    public void makeNewFile() {
        isFileInWork = true;
        currentTaskSet = new TaskSet();
        enableFormButtons();
        taskNumber = 0;
        taskSelector.setItems(FXCollections.observableArrayList());
        addNewTask();
    }

    @FXML
    public void openFile(ActionEvent event) throws IOException {
        FileChooser chooser = new FileChooser(); //діалог збереження
        chooser.setTitle("Choose task file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jTest task set files (.jt)", "*.jt")); //фильтр файлов
        File taskFile = chooser.showOpenDialog(new Stage()); //показ диалога на отдельной сцене
        openTaskFile(taskFile);
    }

    void openFile(File taskFile) throws IOException {
        openTaskFile(taskFile);
    }

    private void removeLineFromFile(File inputFile, String lineToRemove) throws IOException {
        File tempFile = new File("temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        tempFile.renameTo(inputFile);
    }


    private void openTaskFile(File taskFile) throws IOException {
        if(taskFile != null)
        {
            addToLastList(taskFile);
            hasFileSaved = true;
            currentFile = taskFile;
            enableFormButtons();
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
    void onListClicked(MouseEvent event) {
        updateGUI();
    }

    void updateGUI() {
        if (taskNumber > 0) {
            TextArea[] testInputAreas = {t1in, t2in, t3in, t4in, t5in};
            TextArea[] testOutputAreas = {t1out, t2out, t3out, t4out, t5out};

            taskName.setText(currentTaskSet.get(taskSelector.getSelectionModel().getSelectedIndex()).getTaskName());
            taskEdit.setText(currentTaskSet.get(taskSelector.getSelectionModel().getSelectedIndex()).getTaskDescription());

            for (int i = 0; i < 5; i++) {
                testInputAreas[i].setText(currentTaskSet.get(taskSelector.getSelectionModel().getSelectedIndex()).getTaskIn()[i]);
                testOutputAreas[i].setText(currentTaskSet.get(taskSelector.getSelectionModel().getSelectedIndex()).getTaskOut()[i]);
            }

        }
    }

    private void addToLastList (File taskFile) throws IOException {
        File latestList = new File("last.list");
        removeLineFromFile(latestList, taskFile.getAbsolutePath());
        try (Writer listWriter = new PrintWriter(new FileOutputStream(latestList, true))) {
            listWriter.append(taskFile.getAbsolutePath());
        }
    }

    @FXML
    public void saveAsFile(ActionEvent event) {
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
                    addToLastList(currentFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("An error occurred while saving the file!");
                }
            }
        }
    }

    @FXML
    public void saveFile(ActionEvent event) {
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
    public void saveTask(ActionEvent event) {
        int selectedItem = getSelectedItemIndex(taskSelector.getSelectionModel().getSelectedItem());
        saveTaskInTaskSet(selectedItem);
    }

    @FXML
    void removeTask(ActionEvent event) {
        int selectedItem = getSelectedItemIndex(taskSelector.getSelectionModel().getSelectedItem());
        removeTaskFromTaskSet(selectedItem);
    }

    @FXML
    public void setPassword(ActionEvent event) {
        String password = getTextFromTextInputDialog("Password protection", "Enter the password you want to protect the task file:");
        setTaskSetPassword(password);
    }

    @FXML
    public void about(ActionEvent event) throws IOException {
        showInfoAboutApp();
    }

    private void saveTaskInTaskSet(int taskNumber) {
        String[] tasksIn = new String[5];
        String[] tasksOut = new String[5];
        getTasks(tasksIn, t1in, t2in, t3in, t4in, t5in);
        getTasks(tasksOut, t1out, t2out, t3out, t4out, t5out);
        currentTaskSet.set(taskNumber, currentTaskSet.get(taskNumber).setTaskName(taskName.getText()).setTaskIn(tasksIn).setTaskOut(tasksOut).setTaskDescription(taskEdit.getText()));
        updateSelector(taskNumber);
    }

    private void removeTaskFromTaskSet(int taskNumber) {
        currentTaskSet.remove(taskNumber);
        removeTaskFromList(taskSelector.getSelectionModel().getSelectedItem());
        if (taskNumber < 1) removeBtn.setDisable(true);
        taskNumber--;
    }

    private void removeTaskFromList(String item) {
        taskSelector.getItems().remove(item);
    }

    private void getTasks(String[] tasksIn, TextArea t1in, TextArea t2in, TextArea t3in, TextArea t4in, TextArea t5in) {
        tasksIn[0] = t1in.getText();
        tasksIn[1] = t2in.getText();
        tasksIn[2] = t3in.getText();
        tasksIn[3] = t4in.getText();
        tasksIn[4] = t5in.getText();
    }

    private void setTaskSetPassword(String password) {
        if (password.isEmpty()) currentTaskSet.setPasswordProtected(false);
        else {
            currentTaskSet.setPassword(password);
            currentTaskSet.setPasswordProtected(true);
        }
    }

    private String getTextFromTextInputDialog(String title, String contentText) {
        TextInputDialog dialog = makeTextInputDialog(title, contentText);
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().isEmpty()) return "";
        else return result.get();
    }

    private TextInputDialog makeTextInputDialog (String title, String contentText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        return dialog;
    }

    private void showInfoAboutApp() throws IOException {
        FXMLLoader loader = Main.makeLoader("about.fxml");
        Stage stage = Main.startStage(loader, "About jTest Student", 600, 400, false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    private void enableFormButtons() //Керування станом кнопок, які відповідають за зміни в задачах
    {
        taskSelector.setDisable(false);
        addTaskBtn.setDisable(false);
        saveTaskBtn.setDisable(false);
        removeBtn.setDisable(false);
    }

    private void showError(String text)
    {
        Alert error = new Alert(Alert.AlertType.ERROR); //Створення повідомлення про помилку
        error.setTitle("Error");
        error.setContentText(text);
        error.showAndWait();
    }

    private int getSelectedItemIndex(String item)
    {
        for (int i = 0; i < taskSelector.getItems().size(); i++)
        {
            if (taskSelector.getItems().get(i).equals(item)) return i;
        }
        return -1; //У випадку помилки
    }

    private void updateSelector(int selectedItem)
    {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < currentTaskSet.size(); i++) items.add(currentTaskSet.get(i).getTaskName());
        taskSelector.setItems(items);
        taskSelector.getSelectionModel().select(currentTaskSet.get(selectedItem).getTaskName());
        updateGUI();
    }

}
