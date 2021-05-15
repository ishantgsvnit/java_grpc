package com.shopping.db;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderDao {
    private static Logger logger = Logger.getLogger(OrderDao.class.getName());

    public List<Order> getOrders(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        Connection connection = H2DatabaseConnection.getConnectionToDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from orders where user_id =? ");
        preparedStatement.setInt(1, userId);

        ResultSet result = preparedStatement.executeQuery();

        while (result.next()) {
            Order order = new Order();
            order.setOrderId(result.getInt("order_id"));
            order.setUserId(result.getInt("user_id"));
            order.setNoOfItems(result.getInt("no_of_items"));
            order.setOrderDate(result.getDate("order_date"));
            order.setTotalAmount(result.getDouble("total_amount"));
            list.add(order);
        }
        return list;
    }
}
