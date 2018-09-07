package com.revolut.transferservice;

import com.google.gson.Gson;
import com.revolut.transferservice.config.ApplicationProperties;
import com.revolut.transferservice.dao.AccountDao;
import com.revolut.transferservice.dao.TransferAccountDao;
import com.revolut.transferservice.data.DerbyDataSource;
import com.revolut.transferservice.controller.AccountController;
import com.revolut.transferservice.service.TransferAccountService;
import com.revolut.transferservice.utils.resolver.AccountTemplateResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        configure();

        path("/api", () -> {
            post(Routes.TRANSFER.url(), "application/json", (req, res) ->
                    new AccountController(getTransferAccountService()).transfer(req, res), gson::toJson);
        });

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

    private static TransferAccountService getTransferAccountService() {
        AccountDao accountDao = new TransferAccountDao(new AccountTemplateResolver());
        return new TransferAccountService(accountDao);
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Gson gson = new Gson();
}

