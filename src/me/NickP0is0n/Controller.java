package me.NickP0is0n;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    void addNewTask(ActionEvent event) {
        taskNumber++;
        currentTaskSet.add(new Task("Без назви"));
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
        chooser.setTitle("Оберіть файл із завданнями");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файли завдань (.jt)", "*.jt")); //фильтр файлов
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
                showError("Виникла помилка при читанні файлу!");
            }
            taskNumber = currentTaskSet.size();
            updateSelector(0);
        }
    }

    @FXML
    void saveAsFile(ActionEvent event) {
        if (isFileInWork) {
            FileChooser chooser = new FileChooser(); //діалог збереження
            chooser.setTitle("Збережіть файл із завданнями");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файли завдань (.jt)", "*.jt")); //фильтр файлов
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
                    showError("Виникла помилка при створенні файлу!");
                }
            }
            if (currentFile != null) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentFile))) {
                    oos.writeObject(currentTaskSet);
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Виникла помилка при збереженні файлу!");
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
                showError("Виникла помилка при збереженні файлу!");
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
        dialog.setTitle("Встановлення захисту");
        dialog.setContentText("Введіть пароль, яким ви хочете захистити файл завдання:");
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
    void about(ActionEvent event) {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION); //Создание окна ошибки
        aboutAlert.setTitle("Про програму");
        aboutAlert.setHeaderText("jTest Teacher");
        aboutAlert.setGraphic(new ImageView(new File("resources/logo.png").toURI().toString()));
        aboutAlert.setContentText("Версія 1.0.0\n\n" +
                "jTest Teacher є програмою для створення наборів завдань для тестування учнів з інформатики за допомогою jTest.\n\n" +
                "jTest Teacher є частиною програмного комплексу " +
                "jTest для тестування учнів з інформатики.\n" +
                "Початковий код захищено 3-пунктовою ліцензією BSD.");
        aboutAlert.showAndWait();
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
        error.setTitle("Помилка");
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
