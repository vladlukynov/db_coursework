package com.example.app.view.controllers.admin.employees;

import com.example.app.entity.Employee;
import com.example.app.entity.Role;
import com.example.app.service.EmployeeService;
import com.example.app.service.RoleService;
import com.example.app.utils.UIActions;
import com.example.app.view.controllers.admin.AdminController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class EmployeeAddController {
    private final EmployeeService employeeService = new EmployeeService();
    private final RoleService roleService = new RoleService();
    private AdminController adminController;
    @FXML
    private TextField loginLabel;
    @FXML
    private PasswordField passLabel;
    @FXML
    private TextField nameLabel;
    @FXML
    private TextField emailLabel;
    @FXML
    private TextField phoneLabel;
    @FXML
    private ComboBox<Role> roleLabel;
    @FXML
    private TextField postLabel;
    @FXML
    private TextField salaryLabel;
    @FXML
    private DatePicker birthdayLabel;

    @FXML
    public void initialize() {
        roleLabel.setConverter(new StringConverter<>() {
            @Override
            public String toString(Role role) {
                return role.getRoleName();
            }

            @Override
            public Role fromString(String s) {
                return null;
            }
        });

        try {
            roleLabel.getItems().add(roleService.getRoleByName("Администратор"));
            roleLabel.getItems().add(roleService.getRoleByName("Сотрудник"));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).show();
        }
    }

    @FXML
    public void onCloseButtonClick() {
        UIActions.getStage(loginLabel).close();
    }

    @FXML
    public void onRegButtonClick() {
        String login = loginLabel.getText().trim();
        String pass = passLabel.getText();
        String[] name = nameLabel.getText().trim().split(" ");
        String email = emailLabel.getText().trim().trim();
        String phoneNumber = phoneLabel.getText().trim();
        int roleId = roleLabel.getSelectionModel().getSelectedItem().getRoleId();
        double salary;
        String post = postLabel.getText().trim();
        LocalDate birthday = birthdayLabel.getValue();

        try {
            salary = Double.parseDouble(salaryLabel.getText().trim());
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.INFORMATION, "В поле \"зарплата\" ожидалось число", ButtonType.OK).show();
            return;
        }

        if (salary < 0) {
            new Alert(Alert.AlertType.INFORMATION, "В поле \"зарплата\" ожидалось число больше 0", ButtonType.OK).show();
            return;
        }

        if (login.isBlank() || pass.isEmpty() || name.length != 3 || email.isBlank() ||
                phoneNumber.isBlank() || roleId == 0 || post.isBlank() || birthday == null) {
            new Alert(Alert.AlertType.INFORMATION, "Все поля должны быть заполнены!", ButtonType.OK).show();
            return;
        }

        if (Period.between(birthday, LocalDate.now()).getYears() < 18) {
            new Alert(Alert.AlertType.INFORMATION, "Только совершеннолетние пользователи могут зарегистрироваться!", ButtonType.OK).show();
            return;
        }

        try {
            List<Employee> employees = employeeService.getEmployeesInfo();
            if (employees.stream().anyMatch(item -> item.getUserLogin().equals(login))) {
                new Alert(Alert.AlertType.INFORMATION, "Логин занят. Используйте другой!", ButtonType.OK).show();
                return;
            }

            if (employees.stream().anyMatch(item -> item.getEmail().equals(email)) ||
                    employees.stream().anyMatch(item -> item.getPhoneNumber().equals(phoneNumber))) {
                new Alert(Alert.AlertType.INFORMATION, "Почта или телефон уже используются в системе!", ButtonType.OK).show();
            }

            pass = DigestUtils.md5Hex(pass);
            Employee employee = new Employee(login, pass, email, phoneNumber, roleId, true,
                    name[1], name[0], name[2], post, salary, birthday);

            employeeService.registerEmployee(employee);

            adminController.onEmployeesButtonClick();
            UIActions.getStage(loginLabel).close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).show();
        }
    }

    public void setInfo(AdminController controller) {
        adminController = controller;
    }
}
