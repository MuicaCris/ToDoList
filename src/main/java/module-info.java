module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.todolist to javafx.fxml;
    exports com.example.todolist;
    exports com.example.todolist.Controller;
    opens com.example.todolist.Controller to javafx.fxml;
    exports com.example.todolist.Application;
    opens com.example.todolist.Application to javafx.fxml;
}