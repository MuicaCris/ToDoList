package com.example.todolist.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox root;

    @FXML
    private Label errorLogin;

    @FXML
    private Hyperlink registerButton;

    public void initialize() {
        errorLogin.setVisible(false);
    }

    @FXML
    public void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLogin.setText("Username or password cannot be empty");
            errorLogin.setVisible(true);
        } else {
            if (checkCredentials(username, password)) {
                loadMainScreen(username);
            } else {
                errorLogin.setText("Invalid username or password");
                errorLogin.setVisible(true);
            }
        }
    }

    @FXML
    public void onCancelButtonClick() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onRegisterButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/todolist/fxml/register-view.fxml"));
            Parent registerRoot = fxmlLoader.load();

            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene registerScene = new Scene(registerRoot);

            stage.setHeight(295);
            stage.setWidth(320);

            stage.setResizable(false);
            stage.setScene(registerScene);
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkCredentials(String username, String password) {
        File file = new File("src/main/resources/com/example/todolist/file/users.txt");
        if (!file.exists()) {
            System.out.println("The users.txt file does not exist.");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String storedUsername = parts[0];
                    String storedPasswordHash = parts[1];

                    if (storedUsername.equals(username) && storedPasswordHash.equals(hashPassword(password))) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private void loadMainScreen(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todolist/fxml/main-page.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();

            mainController.setUsername(username);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setWidth(810);
            stage.setHeight(516);
            stage.setResizable(false);
            stage.setTitle("Task Manager");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
