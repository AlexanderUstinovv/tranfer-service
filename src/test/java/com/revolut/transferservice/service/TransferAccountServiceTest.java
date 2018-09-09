package com.revolut.transferservice.service;

import com.revolut.transferservice.dao.AccountDao;
import com.revolut.transferservice.model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferAccountServiceTest {

    @Before
    public void setUp() {
        sut = new TransferAccountService(accountDao);
    }

    @After
    public void tearDown() {
        sut = null;
    }

    @Test
    public void testTransfer() {
        long idFrom = 1;
        long idTo = 2;
        doNothing().when(accountDao).update(isA(Account.class));
        when(accountDao.findById(1L)).thenReturn(Optional.of(accountFrom));
        when(accountDao.findById(2L)).thenReturn(Optional.of(accountTo));
        when(accountTo.getId()).thenReturn(1L);
        when(accountTo.getBalance()).thenReturn(BigDecimal.valueOf(33.22));
        when(accountFrom.getId()).thenReturn(2L);
        when(accountFrom.getBalance()).thenReturn(BigDecimal.valueOf(33.22));

        sut.transfer(idFrom, idTo, BigDecimal.valueOf(33.22));

        verify(accountDao, times(1)).update(accountTo);
        verify(accountDao, times(1)).update(accountFrom);
    }

    @Mock
    private AccountDao accountDao;
    @Mock
    private Account accountTo;
    @Mock
    private Account accountFrom;
    private TransferAccountService sut;
}
