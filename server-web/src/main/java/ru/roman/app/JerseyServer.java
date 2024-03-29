package ru.roman.app;


import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

import static ru.roman.app.GuiceConfig.INJECTOR_INSTANCE;

/**
 * Jersey server
 */
public class JerseyServer {
    public static final String BASE_URI = "http://127.0.0.1:8080/";

    /**
     * Starts HTTP server exposing JAX-RS resources defined in this application.
     */
    public static HttpServer start() throws IOException {

        URI uri = URI.create(BASE_URI);

        HttpServer server = HttpServer.create(new InetSocketAddress(uri.getPort()), 0);

        HttpHandler handler = RuntimeDelegate.getInstance()
                .createEndpoint(new JaxRsApp(), HttpHandler.class);
        server.createContext(uri.getPath(), handler);
        server.start();

        return server;
    }

    private static class JaxRsApp extends Application {

        private Set<Object> controllers;

        @Override
        public Set<Object> getSingletons() {
            if (controllers == null) {
                MoneyController controller = INJECTOR_INSTANCE.getInstance(MoneyController.class);

                controllers = Collections.singleton(controller);
            }
            return controllers;
        }
    }
}
