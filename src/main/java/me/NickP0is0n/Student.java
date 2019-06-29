package me.NickP0is0n;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

//класс с информацией и ответами студента
class Student implements Serializable {

    private static final long serialVersionUID = -2298260858404281332L;

    private final String name;
    private final String surName;
    private final String grade;

    private Date startTime;
    private Date finishTime;

    private ArrayList<Integer[]> tasksResults = new ArrayList<>(); //дин. массив с результатами

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
        for (Integer[] tasksResult : tasksResults) {
            doneTasks[tasksResult[0]] = tasksResult[1];
        }
        return doneTasks;
    }
}
