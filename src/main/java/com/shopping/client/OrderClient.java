package com.shopping.client;

import com.shopping.service.OrderServiceImpl;
import com.shopping.stubs.order.Order;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import com.shopping.stubs.order.OrderServiceGrpc;
import io.grpc.Channel;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderClient {
    private static Logger logger = Logger.getLogger(OrderClient.class.getName());

    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingClient;

    public OrderClient(Channel channel) {
        orderServiceBlockingClient = OrderServiceGrpc.newBlockingStub(channel);
    }

    public List<Order> getOrders(int userId) {
        logger.log(Level.INFO, "got orders from order service");
        OrderRequest request = OrderRequest.newBuilder().setUserId(userId).build();
        OrderResponse response = orderServiceBlockingClient.getOrdersForUser(request);
        return response.getOrderList();
    }

}
