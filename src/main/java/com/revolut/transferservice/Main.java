package com.revolut.transferservice;

import com.google.gson.Gson;
import com.revolut.transferservice.config.ApplicationProperties;
import com.revolut.transferservice.data.DerbyDataSource;
import com.revolut.transferservice.controller.AccountController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static spark.Spark.port;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {
        configure();

        post(Routes.TRANSFER.url(), "application/json", (req, res) ->
            new AccountController().transfer(req, res), gson::toJson);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DerbyDataSource.INSTANCE.closePooledConnection();
            } catch (SQLException ex) {
                logger.error("Error in closing connection pool", ex);
            }
        }));
    }

    private static void configure() {
        int port = Integer.valueOf(ApplicationProperties.INSTANCE.getValue("port"));
        port(port);
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Gson gson = new Gson();
}

