package com.example.todolist.Manager;

public class Task {
    private int key;
    private String task;
    private String taskDescription;
    private String status;
    private String priority;

    public Task(int key, String task, String status, String priority) {
        this.key = key;
        this.task = task;
        this.status = status;
        this.priority = priority;
    }

    public String getTask() {
        return task;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public String toString() {
        return task;
    }


}

