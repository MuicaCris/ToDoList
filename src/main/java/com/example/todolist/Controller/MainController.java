package com.example.todolist.Controller;

import com.example.todolist.Control.Filter;
import com.example.todolist.Control.Hash;
import com.example.todolist.Control.Swap;
import com.example.todolist.Manager.Task;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.HashMap;
import java.util.Optional;

public class MainController {
    @FXML
    private VBox taskVBox;

    @FXML
    private ListView<String> taskListView;

    @FXML
    private ListView<String> taskList;

    private Hash taskManager;
    private String username;

    public void setUsername(String username) {
        this.username = username;
        loadTasksFromFile();
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(username + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] taskData = line.split(",");
                if (taskData.length == 4) {
                    String description = taskData[1];
                    String status = taskData[2];
                    String priority = taskData[3];
                    taskManager.add(description, priority);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        taskManager = new Hash();
    }


    private void saveTasksToFile() {
        File file = new File("src/main/resources/com/example/todolist/users/" + username + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            HashMap<Integer, Task> tasks = taskManager.getTasks();
            for (Integer key : tasks.keySet()) {
                Task task = tasks.get(key);
                writer.write(key + "," + task.getTask() + "," + task.getStatus() + "," + task.getPriority());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayTasks() {
        taskListView.getItems().clear();

        HashMap<Integer, Task> tasks = taskManager.getTasks();

        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            String taskText = "Task " + key + ": " + task.getTask()
                    + " [Status: " + task.getStatus() + ", Priority: " + task.getPriority() + "]";
            taskListView.getItems().add(taskText);
        }

        if (tasks.isEmpty()) {
            taskListView.getItems().add("No tasks available.");
        }
    }

    @FXML
    public void handleAddTask() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Task");
        dialog.setHeaderText("Enter Task Description:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            ChoiceDialog<String> priorityDialog = new ChoiceDialog<>("Medium", "High", "Medium", "Low");
            priorityDialog.setTitle("Add Task");
            priorityDialog.setHeaderText("Select Task Priority:");
            priorityDialog.setContentText("Choose priority:");

            Optional<String> priorityResult = priorityDialog.showAndWait();
            priorityResult.ifPresent(priority -> {
                taskManager.add(description, priority);
                saveTasksToFile();
                displayTasks();
            });
        });
    }

    @FXML
    public void handleRemoveTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert("Selection Error", "Please select a task to remove.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to remove the selected task?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> response = confirmationAlert.showAndWait();
        if (response.isEmpty() || response.get() != ButtonType.YES) {
            return;
        }

        int taskId = getTaskIdFromSelectedText(selectedTask);

        if (taskManager.removeTask(taskId)) {
            saveTasksToFile();
            displayTasks();
        } else {
            showAlert("Removal Error", "Task could not be removed.");
        }
    }

    private int getTaskIdFromSelectedText(String selectedTask) {
        String[] parts = selectedTask.split(":");
        String taskIdString = parts[0].replace("Task ", "").trim();
        return Integer.parseInt(taskIdString);
    }

    @FXML
    public void handleRenameTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();

        if (selectedTask == null || selectedTask.isEmpty()) {
            showAlert("Selection Error", "Please select a task to rename.");
            return;
        }

        try {
            int taskId = getTaskIdFromSelectedText(selectedTask);
            Task taskToRename = taskManager.getTask(taskId);

            if (taskToRename == null) {
                showAlert("Rename Error", "Selected task not found.");
                return;
            }

            TextInputDialog renameDialog = new TextInputDialog(taskToRename.getTask());
            renameDialog.setTitle("Rename Task");
            renameDialog.setHeaderText("Enter the new description for the task:");

            Optional<String> result = renameDialog.showAndWait();
            result.ifPresent(newDescription -> {
                taskToRename.setTask(newDescription);
                saveTasksToFile();
                displayTasks();
            });
        } catch (NumberFormatException | NullPointerException e) {
            showAlert("Error", "Unable to retrieve task ID. Please verify the task format.");
        }
    }

    @FXML
    public void handleChangeStatus() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();

        if (selectedTask == null || selectedTask.isEmpty()) {
            showAlert("Selection Error", "Please select a task to change its status.");
            return;
        }

        int taskId = getTaskIdFromSelectedText(selectedTask);
        Task taskToChange = taskManager.getTask(taskId);

        if (taskToChange == null) {
            showAlert("Status Change Error", "Selected task not found.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Pending", "Pending", "In Progress", "Completed");
        dialog.setTitle("Change Task Status");
        dialog.setHeaderText("Select a new status for the task:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newStatus -> {
            taskToChange.setStatus(newStatus);
            saveTasksToFile();
            displayTasks();
        });
    }

    @FXML
    public void handleChangePriority() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert("Selection Error", "Please select a task to change its priority.");
            return;
        }

        try {
            int taskId = getTaskIdFromSelectedText(selectedTask);
            Task taskToChangePriority = taskManager.getTask(taskId);

            if (taskToChangePriority == null) {
                showAlert("Change Priority Error", "Selected task not found.");
                return;
            }

            ChoiceDialog<String> priorityDialog = new ChoiceDialog<>("Medium", "High", "Medium", "Low");
            priorityDialog.setTitle("Change Task Priority");
            priorityDialog.setHeaderText("Select the new priority for the task:");
            Optional<String> result = priorityDialog.showAndWait();

            result.ifPresent(newPriority -> {
                taskToChangePriority.setPriority(newPriority);
                saveTasksToFile();
                displayTasks();
            });

        } catch (NumberFormatException | NullPointerException e) {
            showAlert("Error", "Unable to retrieve task ID. Please verify the task format.");
        }
    }

    @FXML
    public void handleFilterByPriority() {
        ChoiceDialog<String> priorityDialog = new ChoiceDialog<>("Medium", "High", "Medium", "Low");
        priorityDialog.setTitle("Filter Tasks");
        priorityDialog.setHeaderText("Select Priority to Filter:");
        priorityDialog.setContentText("Choose priority:");

        Optional<String> priorityResult = priorityDialog.showAndWait();
        priorityResult.ifPresent(priority -> {
            Filter filter = new Filter(taskManager);
            HashMap<Integer, Task> filteredTasks = new HashMap<>();

            for (Integer key : taskManager.getTasks().keySet()) {
                Task task = taskManager.getTask(key);
                if (task.getPriority().equalsIgnoreCase(priority)) {
                    filteredTasks.put(key, task);
                }
            }

            displayFilteredTasks(filteredTasks);
        });
    }

    private void displayFilteredTasks(HashMap<Integer, Task> filteredTasks) {
        taskVBox.getChildren().clear();

        if (filteredTasks.isEmpty()) {
            Label noTasksLabel = new Label("No tasks found with the specified priority.");
            taskVBox.getChildren().add(noTasksLabel);
            noTasksLabel.setStyle("-fx-text-fill: white;");

        } else {
            for (Integer key : filteredTasks.keySet()) {
                Task task = filteredTasks.get(key);
                Label taskLabel = new Label("Task " + key + ": " + task.getTask()
                        + " [Status: " + task.getStatus() + ", Priority: " + task.getPriority() + "]");
                taskLabel.setStyle("-fx-text-fill: white;");
                taskVBox.getChildren().add(taskLabel);
            }
        }
    }

    @FXML
    public void handleSwapTasks() {
        TextInputDialog dialog1 = new TextInputDialog();
        dialog1.setTitle("Swap Tasks");
        dialog1.setHeaderText("Enter the ID of the first task to swap:");
        Optional<String> result1 = dialog1.showAndWait();

        if (result1.isEmpty()) {
            showAlert("Selection Error", "Please enter a valid task ID.");
            return;
        }

        TextInputDialog dialog2 = new TextInputDialog();
        dialog2.setTitle("Swap Tasks");
        dialog2.setHeaderText("Enter the ID of the second task to swap:");
        Optional<String> result2 = dialog2.showAndWait();

        if (result2.isEmpty()) {
            showAlert("Selection Error", "Please enter a valid task ID.");
            return;
        }

        try {
            int taskId1 = Integer.parseInt(result1.get().trim());
            int taskId2 = Integer.parseInt(result2.get().trim());

            Swap swap = new Swap(taskManager);
            swap.swapTasks(taskId1, taskId2);

            saveTasksToFile();
            displayTasks();

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numeric IDs.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
