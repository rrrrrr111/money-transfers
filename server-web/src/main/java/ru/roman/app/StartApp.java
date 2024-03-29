package ru.roman.app;


import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

import static ru.roman.app.JerseyServer.BASE_URI;

/**
 * Application starting point
 */
public class StartApp {


    public static void main(String[] args) throws IOException {

        HttpServer server = JerseyServer.start();

        System.out.println(String.format("Jersey app started at %s%nHit Enter to stop...", BASE_URI));

        System.in.read();

        server.stop(0);
    }
}
