package com.example.todolist.Control;

import com.example.todolist.Manager.Task;

public class Swap {
    private final Hash taskManager;

    public Swap(Hash taskManager) {
        this.taskManager = taskManager;
    }

    public void swapTasks(int key1, int key2) {
        if (!taskManager.tasks.containsKey(key1) || !taskManager.tasks.containsKey(key2)) {
            System.out.println("One or both task IDs not found for swapping.");
            return;
        }

        Task task1 = taskManager.tasks.get(key1);
        Task task2 = taskManager.tasks.get(key2);

        String tempTask = task1.getTask();
        task1.setTask(task2.getTask());
        task2.setTask(tempTask);

        String tempStatus = task1.getStatus();
        task1.setStatus(task2.getStatus());
        task2.setStatus(tempStatus);

        String tempPriority = task1.getPriority();
        task1.setPriority(task2.getPriority());
        task2.setPriority(tempPriority);

        System.out.println("Tasks have been swapped.");
        for (Integer key : taskManager.tasks.keySet()) {
            Task task = taskManager.tasks.get(key);
            System.out.println(key + ": " + task.getTask() + " [" + task.getStatus() + "]" + " Priority: - " + task.getPriority());
        }
    }
}
