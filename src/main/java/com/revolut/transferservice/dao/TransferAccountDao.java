package com.revolut.transferservice.dao;

import com.revolut.transferservice.data.QueryExecutor;
import com.revolut.transferservice.model.Account;
import com.revolut.transferservice.utils.resolver.TemplateResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TransferAccountDao extends QueryExecutor implements AccountDao {

    public TransferAccountDao(TemplateResolver<Account> accountTemplateResolver) {
        this.accountTemplateResolver = accountTemplateResolver;
    }

    public Optional<Account> findById(long id) {
        String sqlQuery = accountTemplateResolver.resolveTemplate("queries/read_account.vm", id);
        Optional<Account> accountResult = Optional.empty();
        try {
            List<HashMap<String, Object>> results = executeQuery(sqlQuery);
            if (results.size() > 0) {
                long resultId = ((Number) results.get(0).get("ID")).longValue();
                double amount = ((Number) results.get(0).get("BALANCE")).doubleValue();
                Account account = new Account(resultId, amount);
                accountResult = Optional.of(account);
            }
        } catch (SQLException ex) {
            logger.error("Error in reading from database", ex);
        }
        return accountResult;
    }

    public void update(Account account) {
        String sqlQuery = accountTemplateResolver.resolveTemplate("queries/update_account.vm", account);
        try {
            executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            logger.error("Error in updating account", ex);
        }
    }

    private TemplateResolver<Account> accountTemplateResolver;
    private final Logger logger = LoggerFactory.getLogger(TransferAccountDao.class);
}
