package com.shopping.service;

import com.shopping.client.OrderClient;
import com.shopping.db.OrderDao;
import com.shopping.db.User;
import com.shopping.db.UserDao;
import com.shopping.stubs.order.Order;
import com.shopping.stubs.user.*;
import com.shopping.stubs.user.UserRequest;
import com.shopping.stubs.user.UserResponse;
import com.shopping.stubs.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    UserDao userDao = new UserDao();

    @Override
    public void getUserDetails(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        User user = userDao.getUserDetails(request.getUsername());
        UserResponse.Builder builder = UserResponse.newBuilder()
                .setId(user.getId())
                .setAge(user.getAge())
                .setName(user.getName())
                .setUsername(user.getUsername())
                .setGender(com.shopping.stubs.user.Gender.valueOf(user.getGender()));


        List<Order> list = getOrders(user);
        builder.setNoOfOrders(list.size());

        UserResponse res = builder.build();
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

    private List<Order> getOrders(User user) {
        logger.log(Level.INFO,"creating channel");
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:50052")
                .usePlaintext().build();
        OrderClient orderClient = new OrderClient(managedChannel);
        List<Order> list = orderClient.getOrders(user.getId());
        try {
            managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.log(Level.SEVERE,"exception" , e);
        }
        return list;
    }
}
