module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.app to javafx.fxml;
    opens com.example.app.view.controllers.auth to javafx.fxml;
    opens com.example.app.view.controllers.admin to javafx.fxml;
    opens com.example.app.view.controllers.admin.employees to javafx.fxml;
    opens com.example.app.view.controllers.admin.cars to javafx.fxml;
    opens com.example.app.view.controllers.admin.services to javafx.fxml;
    opens com.example.app.view.controllers.admin.details to javafx.fxml;

    exports com.example.app;
    exports com.example.app.entity;
    exports com.example.app.view.controllers.auth;
    exports com.example.app.view.controllers.admin;
    exports com.example.app.exception;
    exports com.example.app.view.controllers.admin.employees;
    exports com.example.app.view.controllers.admin.cars;
    exports com.example.app.view.controllers.admin.services;
    exports com.example.app.view.controllers.admin.details;
}
