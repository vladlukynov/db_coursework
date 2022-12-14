package com.example.app.view.controllers.admin.details;

import com.example.app.entity.Detail;
import com.example.app.service.DetailService;
import com.example.app.utils.UIActions;
import com.example.app.view.controllers.admin.AdminController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class DetailsEditController {
    private Detail detail;
    private final DetailService detailService = new DetailService();
    private AdminController adminController;
    @FXML
    private TextField nameLabel;
    @FXML
    private TextField priceLabel;
    @FXML
    private TextField quantityLabel;

    @FXML
    public void onCancelButtonClick() {
        UIActions.getStage(nameLabel).close();
    }

    @FXML
    public void onApplyButtonClick() {
        String name = nameLabel.getText().trim();
        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceLabel.getText().trim());
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.INFORMATION, "Поле стоимость должно быть числом").show();
            return;
        }
        try {
            quantity = Integer.parseInt(quantityLabel.getText().trim());
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.INFORMATION, "Поле количество должно быть числом", ButtonType.OK).show();
            return;
        }
        if (quantity < 0 || price < 0) {
            new Alert(Alert.AlertType.INFORMATION, "Поля стоимость и количество не могут быть меньше 0", ButtonType.OK).show();
            return;
        }
        if (name.isBlank()) {
            new Alert(Alert.AlertType.INFORMATION, "Название не может быть пустым", ButtonType.OK).show();
            return;
        }
        try {
            Detail newDetail = new Detail(name, price, quantity);
            detailService.updateDetail(detail.getDetailId(), newDetail);
            adminController.onDetailsButtonClick();
            UIActions.getStage(nameLabel).close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).show();
        }
    }

    public void setInfo(Detail detail, AdminController controller) {
        this.detail = detail;
        adminController = controller;

        nameLabel.setText(detail.getDetailName());
        priceLabel.setText(String.valueOf(detail.getPrice()));
        quantityLabel.setText(String.valueOf(detail.getQuantity()));
    }
}
