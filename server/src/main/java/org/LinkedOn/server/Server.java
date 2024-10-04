package org.LinkedOn.server;

import com.sun.net.httpserver.HttpServer;
import org.LinkedOn.server.routers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/media", MediaRouter.getInstance());
            server.createContext("/users", UserRouter.getInstance());
            server.createContext("/educations", EducationRouter.getInstance());
            server.createContext("/follows", FollowRouter.getInstance());
            server.createContext("/connects", ConnectRouter.getInstance());
            server.createContext("/posts", PostRouter.getInstance());
            server.createContext("/messages", MessageRouter.getInstance());
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
