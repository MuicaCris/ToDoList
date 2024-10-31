package com.example.todolist;

import com.example.todolist.Application.MainApplication;
import javafx.stage.Stage;


public class Main {
    public static void main(String[] args) {
        MainApplication mainApplication = new MainApplication();
        Stage stage = new Stage();

        mainApplication.start(stage);
    }
}