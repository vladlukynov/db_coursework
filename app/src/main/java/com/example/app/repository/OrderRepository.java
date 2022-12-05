package com.example.app.repository;

import com.example.app.entity.Order;

import java.sql.*;
import java.util.*;

import static com.example.app.utils.DatabaseAuth.*;

public class OrderRepository {
    public List<Order> getOrders() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, userName, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM GetOrdersWithSum")) {
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order(resultSet.getInt("OrderId"),
                        resultSet.getInt("CarId"),
                        resultSet.getString("CarNumber"),
                        resultSet.getString("Status"),
                        resultSet.getDate("CreationDate").toLocalDate(),
                        resultSet.getDate("StatusChangeDate").toLocalDate(),
                        resultSet.getString("EmployeeLogin"),
                        resultSet.getString("ClientLogin"),
                        resultSet.getString("Element"),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("ElementSum"),
                        resultSet.getDouble("Total"));

                orders.add(order);
            }
            return orders;
        }
    }
}