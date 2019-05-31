package me.NickP0is0n;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

//класс с информацией и ответами студента
public class Student implements Serializable {

    private static final long serialVersionUID = -2298260858404281332L;

    private String name;
    private String surName;
    private String grade;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    private Date startTime;
    private Date finishTime;

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getGrade() {
        return grade;
    }

    public ArrayList<Integer[]> getTasksResults() {
        return tasksResults;
    }

    private ArrayList<Integer[]> tasksResults = new ArrayList<>(); //дин. массив с результатами

    Student(String name, String surName, String grade) {
        this.name = name;
        this.surName = surName;
        this.grade = grade;
    }

    void addTask(int taskNumber, int goodNumber) //добавляем выполненное задание с количеством пройденных тестов
    {
        Integer[] data = {taskNumber, goodNumber};
        tasksResults.add(data);
    }
    int[] getDoneTasks()
    {
        int[] doneTasks = new int[tasksResults.size()];
        for (int i = 0; i < tasksResults.size(); i++) {
            doneTasks[tasksResults.get(i)[0]] = tasksResults.get(i)[1];
        }
        return doneTasks;
    }
}
