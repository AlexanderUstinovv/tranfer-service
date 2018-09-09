package com.revolut.transferservice.controller;

import com.revolut.transferservice.dao.TransferAccountDao;
import com.revolut.transferservice.data.DerbyDataSource;
import com.revolut.transferservice.service.TransferAccountService;
import com.revolut.transferservice.utils.resolver.AccountTemplateResolver;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import spark.Request;
import spark.Response;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerIT {

    @BeforeClass
    public static void setUpOnce() throws SQLException, IOException {

        try (Connection connection = DerbyDataSource.INSTANCE.getConnection()) {
            String sqlQuery = getFile("queries/add-accounts.sql");
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.setBigDecimal(1, BigDecimal.valueOf(42.12));
                preparedStatement.executeUpdate();
                preparedStatement.setBigDecimal(1, BigDecimal.valueOf(52.55));
                preparedStatement.executeUpdate();
            }
        }
    }

    @AfterClass
    public static void tearDownOnce() throws SQLException, IOException {

        try (Connection connection = DerbyDataSource.INSTANCE.getConnection()) {
            String sqlQuery = getFile("queries/delete-accounts.sql");
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.executeUpdate();
            }
        }

        DerbyDataSource.INSTANCE.closePooledConnection();
    }

    @Before
    public void setUp() {
        transferAccountDao = new TransferAccountDao(new AccountTemplateResolver());
        TransferAccountService transferAccountService = new TransferAccountService(transferAccountDao);
        sut = new AccountController(transferAccountService);
    }

    @After
    public void tearDown() {
        sut = null;
    }


    @Test
    public void testTransfer() {
        long firstId = 1;
        long secondId = 2;
        BigDecimal amount = BigDecimal.valueOf(3.22);
        BigDecimal firstAmount = transferAccountDao.findById(firstId).get().getBalance().subtract(amount);
        BigDecimal secondAmount = transferAccountDao.findById(secondId).get().getBalance().add(amount);
        when(request.body()).thenReturn(
                String.format("{\"idFrom\": %d,\"idTo\": %d,\"amount\": %f%n}", firstId ,secondId, amount)
        );

        sut.transfer(request, response);

        Assert.assertEquals(transferAccountDao.findById(firstId).get().getBalance().setScale(2, RoundingMode.CEILING), firstAmount);
        Assert.assertEquals(transferAccountDao.findById(secondId).get().getBalance().setScale(2, RoundingMode.CEILING), secondAmount);
    }

    private static String getFile(String fileName) throws IOException {

        ClassLoader classLoader = AccountControllerIT.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getPath());

        StringBuilder stringBuilder = new StringBuilder();
        Files.lines(file.toPath())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    @Mock
    private Request request;
    @Mock
    private Response response;
    private TransferAccountDao transferAccountDao;
    private AccountController sut;
}
