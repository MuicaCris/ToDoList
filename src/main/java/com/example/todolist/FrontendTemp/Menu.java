package com.example.todolist.FrontendTemp;

import com.example.todolist.Control.Filter;
import com.example.todolist.Control.Hash;
import com.example.todolist.Control.Swap;

import java.util.Scanner;

public class Menu {
    private boolean exit = false;
    private final Hash taskManager = new Hash();
    private final Filter filter = new Filter(taskManager);
    private final Swap swap = new Swap(taskManager);

    public void menu() {
        int choice;
        int choice1;
        Scanner scan = new Scanner(System.in);

        do {
            System.out.println("\n----------------To do List----------------");
            System.out.println("1. Add task.");
            System.out.println("2. Remove task.");
            System.out.println("3. Rename task.");
            System.out.println("4. Change status.");
            System.out.println("5. Change priority");
            System.out.println("6. Filter by priority");
            System.out.println("7. Swap tasks");
            System.out.println("8. Exit.");

            choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Write down a task:");
                    String task = scan.nextLine();

                    clearScreen();
                    String priority = "None";
                    taskManager.add(task, priority);

                    break;

                case 2:
                    System.out.print("Enter the task ID you want to remove: ");
                    int keyToRemove = scan.nextInt();

                    clearScreen();
                    taskManager.remove(keyToRemove);

                    break;

                case 3:
                    System.out.print("Enter the task ID you want to rename: ");
                    int keyToRename = scan.nextInt();
                    scan.nextLine();

                    String newTaskDescription = scan.nextLine();

                    clearScreen();
                    taskManager.rename(keyToRename, newTaskDescription);

                    break;

                case 4:
                    System.out.print("Enter the task ID to change the status: ");
                    int keyToChangeStatus = scan.nextInt();

                    System.out.println("1. Unfinished");
                    System.out.println("2. In progress");
                    System.out.println("3. Finished");

                    choice1 = scan.nextInt();
                    scan.nextLine();

                    String newStatus = switch (choice1) {
                        case 1 -> "Unfinished";
                        case 2 -> "In progress";
                        case 3 -> "Finished";
                        default -> "Unfinished";
                    };

                    clearScreen();
                    taskManager.changeStatus(keyToChangeStatus, newStatus);

                    break;

                case 5:
                    System.out.println("Enter task ID to change priority:");
                    int keyToChangePriority = scan.nextInt();

                    System.out.println("1. Low");
                    System.out.println("2. Medium");
                    System.out.println("3. High");

                    choice1 = scan.nextInt();
                    scan.nextLine();

                    String newPriority = switch (choice1) {
                        case 1 -> "Low";
                        case 2 -> "Medium";
                        case 3 -> "High";
                        default -> "None";
                    };
                    taskManager.changeTaskPriority(keyToChangePriority, newPriority);
                    break;

                case 6:
                    System.out.println("Enter the priority to filter by:");
                    System.out.println("1. Low");
                    System.out.println("2. Medium");
                    System.out.println("3. High");

                    choice1 = scan.nextInt();
                    scan.nextLine();

                    String filterPriority = switch (choice1) {
                        case 1 -> "Low";
                        case 2 -> "Medium";
                        case 3 -> "High";
                        default -> "";
                    };

                    filter.filterByPriority(filterPriority);

                    break;

                case 7:
                    System.out.println("Enter the ID of the first task to swap:");
                    int key1 = scan.nextInt();
                    System.out.println("Enter the ID of the second task to swap:");
                    int key2 = scan.nextInt();

                    swap.swapTasks(key1, key2);
                    break;

                case 8:
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }while(!exit);
    }
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}


