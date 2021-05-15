package com.shopping.server;

import com.shopping.service.OrderServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServer {

    private static final Logger logger = Logger.getLogger(OrderServer.class.getName());
    private Server server;

    public void startServer() {
        try {
            int port = 50052;
            server = ServerBuilder.forPort(port)
                    .addService(new OrderServiceImpl())
                    .build()
                    .start();
            logger.log(Level.SEVERE, "Server started 50052");

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                      stopServer();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Server shut down interrupted");
                    }
                }
            });


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server didn't start");
        }
    }

    public void stopServer() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutDown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OrderServer userServer = new OrderServer();
        userServer.startServer();
        userServer.blockUntilShutDown();
    }


}
