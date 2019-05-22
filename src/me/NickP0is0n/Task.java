package me.NickP0is0n;

import java.io.Serializable;

public class Task implements Serializable {

    private String taskName;
    private String taskDescription;

    public String getTaskDescription() {
        return taskDescription;
    }

    public Task setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
        return this;
    }

    private String[] taskIn = new String[5];
    private String[] taskOut = new String[5];

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public Task setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String[] getTaskIn() {
        return taskIn;
    }

    public Task setTaskIn(String[] taskIn) {
        this.taskIn = taskIn;
        return this;
    }

    public String[] getTaskOut() {
        return taskOut;
    }

    public Task setTaskOut(String[] taskOut) {
        this.taskOut = taskOut;
        return this;
    }
}
