package com.example.todolist.Control;

import com.example.todolist.Manager.Task;

import java.util.HashMap;

public class Filter {
    private final Hash taskManager;

    public Filter(Hash taskManager) {
        this.taskManager = taskManager;
    }

    public void filterByPriority(String priority) {
        HashMap<Integer, Task> tasks = taskManager.getTasks();

        System.out.println("Tasks with priority: " + priority);

        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);

            if (task.getPriority().equalsIgnoreCase(priority)) {
                System.out.println(key + ": " + task.getTask() + " [" + task.getStatus() + "]" + " Priority: - " + task.getPriority());
            }
        }
    }
}

