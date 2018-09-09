package com.revolut.transferservice.controller;

import com.revolut.transferservice.service.TransferAccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @Before
    public void setUp() {
        sut = new AccountController(transferAccountService);
    }

    @After
    public void tearDown() {
        sut = null;
    }

    @Test
    public void testTransfer() {
        doNothing().when(transferAccountService).transfer(isA(Long.class), isA(Long.class), isA(BigDecimal.class));
        when(request.body()).thenReturn("{\"idFrom\": 1,\"idTo\": 2,\"amount\": 3.22}");
        sut.transfer(request, response);
        verify(transferAccountService, times(1)).transfer(1, 2, BigDecimal.valueOf(3.22));
    }

    @Mock
    private TransferAccountService transferAccountService;
    @Mock
    private Request request;
    @Mock
    private Response response;

    private AccountController sut;
}
