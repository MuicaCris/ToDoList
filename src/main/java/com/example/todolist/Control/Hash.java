package com.example.todolist.Control;

import com.example.todolist.Manager.Task;

import java.util.HashMap;

public class Hash {
    HashMap<Integer, Task> tasks = new HashMap<>();
    private int currentKey = 1;

    public void add(String taskDescription, String priority) {
        Task t = new Task(currentKey, taskDescription, "Unfinished", priority);
        tasks.put(currentKey, t);
        currentKey++;

        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            System.out.println(key + ": " + task.getTask() + " [" + task.getStatus() + "]" + " Priority: - " + task.getPriority());
        }
    }

    public boolean removeTask(int taskId) {
        return tasks.remove(taskId) != null;
    }

    public void remove(int keyToRemove) {
        if (!tasks.containsKey(keyToRemove)) {
            System.out.println("ID not found");
            return;
        }

        System.out.println("Removing task with key: " + keyToRemove);
        tasks.remove(keyToRemove);

        HashMap<Integer, Task> updatedTasks = new HashMap<>();

        for (Integer key : tasks.keySet()) {
            if (key > keyToRemove) {
                Task task = tasks.get(key);
                updatedTasks.put(key - 1, task);
            } else {
                updatedTasks.put(key, tasks.get(key));
            }
        }

        tasks = updatedTasks;
        currentKey--;

        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            System.out.println(key + ": " + task.getTask() + " [" + task.getStatus() + "]" + " Priority: - " + task.getPriority());
        }
    }

    public boolean renameTask(int taskId, String newDescription) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setTask(newDescription);
            return true;
        }
        return false;
    }

    public void rename(int keyToModify, String newTaskDescription) {
        if (!tasks.containsKey(keyToModify)) {
            System.out.println("ID not found for rename");
            return;
        }

        Task t = tasks.get(keyToModify);
        t.setTask(newTaskDescription);
        t.setStatus("Unfinished");

        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            System.out.println(key + ": " + task.getTask() + " [" + task.getStatus() + "]" + " Priority: - " + task.getPriority());
        }
    }

    public void changeStatus(int keyToChange, String newStatus) {
        if (!tasks.containsKey(keyToChange)) {
            System.out.println("ID not found for status change");
            return;
        }

        Task t = tasks.get(keyToChange);
        t.setStatus(newStatus);

        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            System.out.println(key + ": " + task.getTask() + " [" + task.getStatus() + "]" + " Priority: - " + task.getPriority());
        }

    }

    public void changeTaskPriority(int key, String newPriority) {
        if (!tasks.containsKey(key)) {
            System.out.println("ID not found for priority change");
            return;
        }
        Task task = tasks.get(key);
        task.setPriority(newPriority);

        for (Integer i : tasks.keySet()) {
            task = tasks.get(i);
            System.out.println(i + ": " + task.getTask() + " [" + task.getStatus() + "]" + " Priority: - " + task.getPriority());
        }
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }
}
