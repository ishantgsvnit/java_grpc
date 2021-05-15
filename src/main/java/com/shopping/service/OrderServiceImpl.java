
package com.shopping.service;

import com.google.protobuf.util.Timestamps;
import com.shopping.db.Order;
import com.shopping.db.OrderDao;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import com.shopping.stubs.user.UserResponse;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class OrderServiceImpl extends com.shopping.stubs.order.OrderServiceGrpc.OrderServiceImplBase {
    OrderDao orderDao = new OrderDao();
    private static Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    @Override
    public void getOrdersForUser(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        try {
            List<Order> orders = orderDao.getOrders(request.getUserId());
            logger.log(Level.INFO,"got orders from DAO and converted to Order");
            List<com.shopping.stubs.order.Order> list = orders.stream().map(order -> com.shopping.stubs.order.Order.newBuilder()
                    .setOrderDate(Timestamps.fromMillis(order.getOrderDate().getTime()))
                    .setUserId(order.getUserId())
                    .setOrderId(order.getOrderId())
                    .setNoOfItems(order.getNoOfItems())
                    .setTotalAmount(order.getTotalAmount())
                    .build()).collect(Collectors.toList());

            OrderResponse res = OrderResponse.newBuilder().addAllOrder(list).build();
            responseObserver.onNext(res);
            responseObserver.onCompleted();

        } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
        }

    }
}
