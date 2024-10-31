package com.example.todolist.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterController {

    @FXML
    public Hyperlink loginButton;

    @FXML
    public Label passwordNotMatch;

    @FXML
    public Label passwordNull;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField rePasswordField;

    @FXML
    private Label errorRegister;

    private static final String USERS_FILE = "src/main/resources/com/example/todolist/file/users.txt";

    @FXML
    private void initialize() {
        errorRegister.setVisible(false);
    }

    @FXML
    public void initializeNull() {
        passwordNull.setVisible(false);
    }

    @FXML
    public void initializeMatch() {
        passwordNotMatch.setVisible(false);
    }

    public void onRegisterButtonClic() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = rePasswordField.getText();

        if (isUsernameTaken(username)) {
            errorRegister.setText("Username already taken!");
            errorRegister.setVisible(true);
            return;
        }

        if (password.equals(confirmPassword)) {
            try {
                VBox loginPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/todolist/fxml/login-view.fxml")));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(loginPage));

                stage.setWidth(320);
                stage.setHeight(265);

                stage.setResizable(false);

                saveUser(username, hashPassword(password));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (password.isEmpty()) {
            errorRegister.setText("Password cannot be empty!");
            errorRegister.setVisible(true);
        } else {
            errorRegister.setText("Passwords are not matching!");
            errorRegister.setVisible(true);
        }
    }


    @FXML
    public void onCancelButtonClick() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onLoginButtonClick(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todolist/fxml/login-view.fxml"));
            Parent loginView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loginView);

            stage.setHeight(265);
            stage.setWidth(320);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveUser(String username, String hashedPassword) {
        try {
            File userDir = new File("src/main/resources/com/example/todolist/users");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                writer.write(username + ":" + hashedPassword);
                writer.newLine();
            }catch (IOException e) {
                e.printStackTrace();
            }

            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            File userFile = new File(userDir, username + ".txt");

            if (userFile.exists()) {
                System.out.println("User already exists.");
            }

            try (BufferedWriter userWriter = new BufferedWriter(new FileWriter(userFile))) {

            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception e) {
            e.printStackTrace();
    }
}


    private boolean isUsernameTaken(String username) {
        Map<String, String> users = readUsers();
        return users.containsKey(username);
    }

    private Map<String, String> readUsers() {
        Map<String, String> users = new HashMap<>();

        if (!Files.exists(Paths.get(USERS_FILE)))
            return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))){
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");

                if (parts.length == 2)
                    users.put(parts[0], parts[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }


    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes)
                sb.append(String.format("%02x", b));

            return sb.toString();
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not fount");
        }
    }
}
