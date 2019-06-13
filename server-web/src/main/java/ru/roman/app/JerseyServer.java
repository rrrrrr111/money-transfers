package ru.roman.app;


import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Set;

/**
 * Jersey server
 */
class JerseyServer {
    private static final String BASE_URI = "http://127.0.0.1:8080/";

    /**
     * Starts HTTP server exposing JAX-RS resources defined in this application.
     */
    static void start() {
        try {

            HttpServer server = startServer();
            System.out.println(String.format("Jersey app started at %s%n" +
                    "Hit enter to stop...", BASE_URI));

            System.in.read();
            server.stop(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpServer startServer() throws IOException {
        URI uri = URI.create(BASE_URI);

        HttpServer server = HttpServer.create(new InetSocketAddress(uri.getPort()), 0);

        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new JaxRsApp(), HttpHandler.class);
        server.createContext(uri.getPath(), handler);

        return server;
    }

    private static class JaxRsApp extends Application {
        private final Set<Class<?>> controllers = Set.of();

        @Override
        public Set<Class<?>> getClasses() {
            return controllers;
        }
    }
}
